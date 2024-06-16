package com.github.test.application.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.test.application.R
import com.github.test.application.domain.dataclasses.DataRepository
import com.github.test.application.domain.dataclasses.DataRepositoryContentEntry
import com.github.test.application.databinding.FragmentRepositoryContentBinding
import com.github.test.application.presentation.adapters.RepositoryContentsAdapter
import com.github.test.application.presentation.viewmodel.RepositoryContentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.nio.file.Paths

@AndroidEntryPoint
class RepositoryContentFragment : Fragment() {
    private val viewModelRepositoryContent: RepositoryContentViewModel by activityViewModels()
    private lateinit var repository: DataRepository
    private var pathFolder = Paths.get("")
    private var _binding: FragmentRepositoryContentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                clickButtonBack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        _binding = FragmentRepositoryContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserves()
        setListeners()
    }

    private fun setObserves(){
        viewModelRepositoryContent.isLoading.observe(viewLifecycleOwner) {
            // Обновление UI в зависимости от состояния загрузки
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModelRepositoryContent.dataRepository.observe(viewLifecycleOwner) {
            repository = it
            //Запуск проверки на  интернет
            viewModelRepositoryContent.checkInternet()
        }

        viewModelRepositoryContent.isInternet.observe(viewLifecycleOwner) {
            if (it) {
                binding.layoutError.visibility = View.GONE
                //Интернет есть, загружаем содержимое
                viewModelRepositoryContent.getRepositoryContent(repository.owner.login, repository.name, pathFolder.toString())
            } else {
                //Интернета нет, показываем ошибку
                showError(getString(R.string.no_internet))
            }
        }

        viewModelRepositoryContent.errorMessage.observe(viewLifecycleOwner) {
            // Показ сообщений об ошибках
            showError(it)
        }

        viewModelRepositoryContent.listRepositoryContent.observe(viewLifecycleOwner) {
            // Показ содержимого
            setRecyclerView(it)
        }
    }

    private fun setListeners(){
        binding.buttonTryAgain.setOnClickListener{
            binding.progressBar.visibility = View.VISIBLE
            viewModelRepositoryContent.checkInternet()
        }

        binding.buttonBack.setOnClickListener{
            clickButtonBack()
        }
    }

    private fun setRecyclerView(list: List<DataRepositoryContentEntry>) {
        val adapter = RepositoryContentsAdapter(list) { itemFromList ->
            if (itemFromList.type == getString(R.string.sign_folder)){
                navigateToFolder(itemFromList.name)
            }else{
                openFile(itemFromList.html_url)
            }
        }
        binding.recyclerViewList.isNestedScrollingEnabled = false
        binding.recyclerViewList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewList.adapter = adapter
    }

    private fun navigateToFolder(folderPath: String) {
        pathFolder = pathFolder.resolve(folderPath)
        viewModelRepositoryContent.getRepositoryContent(repository.owner.login, repository.name, pathFolder.toString())
    }

    private fun openFile(htmlUrl:String) {
        val bundle = Bundle()
        bundle.putString(getString(R.string.html_url_key), htmlUrl)

        val webViewFragment = WebViewFragment()
        webViewFragment.arguments = bundle

        viewModelRepositoryContent.clearList()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, webViewFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun clickButtonBack(){
        if (pathFolder.toString()==""){
            parentFragmentManager.popBackStack()
        }else{
            pathFolder = pathFolder.parent ?: Paths.get("")
            viewModelRepositoryContent.getRepositoryContent(repository.owner.login, repository.name, pathFolder.toString())
        }
        viewModelRepositoryContent.clearList()
    }

    private fun showError(textError:String){
        //Убираем загрузку
        binding.progressBar.visibility = View.GONE
        //Показываем ошибку
        binding.layoutError.visibility = View.VISIBLE
        binding.textError.text = textError
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}