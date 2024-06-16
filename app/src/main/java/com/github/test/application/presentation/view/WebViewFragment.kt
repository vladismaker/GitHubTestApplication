package com.github.test.application.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.airbnb.lottie.LottieAnimationView
import com.github.test.application.R
import com.github.test.application.databinding.FragmentWebViewBinding
import com.github.test.application.presentation.viewmodel.WebViewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewFragment : Fragment() {
    private val viewModelWebView:WebViewViewModel by viewModels()
    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelWebView.checkInternet()

        val htmlUrl = arguments?.getString(getString(R.string.html_url_key))

        viewModelWebView.isInternet.observe(viewLifecycleOwner) {
            if (it) {
                binding.layoutError.visibility = View.GONE
                //Интернет есть
                binding.contentFromFile.webViewClient = webViewClient(binding.contentFromFile, binding.lottieProgressBar)

                if (htmlUrl!= null){
                    //Ссылка есть
                    binding.contentFromFile.loadUrl(htmlUrl)
                }else{
                    //Ошибка, пустая ссылка
                    showError(getString(R.string.loading_error))
                }
            } else {
                //Интернета нет, показываем ошибку
                showError(getString(R.string.no_internet))
            }
        }

        viewModelWebView.errorMessage.observe(viewLifecycleOwner) {
            //Показываем ошибку
            showError(it)
        }

        binding.buttonTryAgain.setOnClickListener{
            binding.lottieProgressBar.visibility = View.VISIBLE
            viewModelWebView.checkInternet()
        }
    }

    private fun webViewClient(contentFileWebView:WebView, lottieProgressBar:LottieAnimationView): WebViewClient {
        return object : WebViewClient() {
            override fun onPageCommitVisible(viewCoPeen: WebView, urlCoPeen: String) {
                super.onPageCommitVisible(viewCoPeen, urlCoPeen)
                contentFileWebView.visibility = View.VISIBLE
                lottieProgressBar.visibility = View.GONE
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {

            }
            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                // Обработка HTTP-ошибки
                showError("Ошибка загрузки:${errorResponse?.statusCode}")
            }
        }
    }

    private fun showError(textError:String){
        //Убираем загрузку
        binding.lottieProgressBar.visibility = View.GONE
        //Показываем ошибку
        binding.layoutError.visibility = View.VISIBLE
        binding.textError.text = textError
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}