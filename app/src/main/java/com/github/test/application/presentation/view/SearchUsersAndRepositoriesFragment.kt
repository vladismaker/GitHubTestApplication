package com.github.test.application.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.test.application.R
import com.github.test.application.databinding.FragmentSearchUsersAndRepositoriesBinding
import com.github.test.application.presentation.adapters.SearchResultAdapter
import com.github.test.application.presentation.viewmodel.RepositoryContentViewModel
import com.github.test.application.presentation.viewmodel.SearchUsersAndRepositoriesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchUsersAndRepositoriesFragment : Fragment() {
    private val viewModelRepositoryContent: RepositoryContentViewModel by activityViewModels()
    private val viewModelSearch: SearchUsersAndRepositoriesViewModel by viewModels()
    private val adapter = SearchResultAdapter { repository ->
        viewModelRepositoryContent.setDataRepositiryToRepositoryContent(repository)
        viewModelRepositoryContent.dataRepository.observe(viewLifecycleOwner) {
            // Переходим на следующий фрагмент только после обновления LiveData
            viewModelSearch.clearList()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, RepositoryContentFragment())
                .addToBackStack(null)
                .commit()
        }
    }
    private var querySearch = ""
    private var _binding: FragmentSearchUsersAndRepositoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchUsersAndRepositoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()

        setObservers()

        setListeners()

        setSearchView()
    }

    private fun setObservers(){
        viewModelSearch.searchResults.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        viewModelSearch.isLoading.observe(viewLifecycleOwner){
            // Обновление UI в зависимости от состояния загрузки
            if (it){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModelSearch.errorMessage.observe(viewLifecycleOwner){
            // Показ сообщений об ошибках
            showError(it)
        }

        viewModelSearch.isInternet.observe(viewLifecycleOwner) {
            if (it) {
                binding.layoutError.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                if (querySearch.length>=3){
                    viewModelSearch.performSearch(querySearch)
                }
            } else {
                //Интернета нет, показываем ошибку
                showError(getString(R.string.no_internet))
            }
        }
    }

    private fun setListeners(){
        binding.buttonTryAgain.setOnClickListener{
            binding.progressBar.visibility = View.VISIBLE
            viewModelSearch.checkInternet()
        }

    }

    private fun setSearchView(){
        binding.searchView.isIconified = false
        binding.searchView.isFocusable = true
        binding.searchView.requestFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.length<3) return true
                    viewModelSearch.clearList()
                    querySearch = it
                    viewModelSearch.checkInternet()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setRecyclerView() {
        binding.recyclerViewList.isNestedScrollingEnabled = false
        binding.recyclerViewList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewList.adapter = adapter
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