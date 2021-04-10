/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import github.karchx.wiki.databinding.ArticleFragmentBinding
/*
  ViewBinding HowTo
  Документация по ViewBinding лежит https://developer.android.com/topic/libraries/view-binding
  Там все довольно просто, коротко и достаточно для понимания (подробностей особых нет).
  ViewBinding используется, чтобы ссылаться на View (Button, TextView...) без findViewById,
  это экономит CPU во время работы приложения (не нужно делать поиск) и еще ViewBinding
  удобнее.
  Для работы с ViewBinding его нужно
  1. разрешить для проекта
  2. возможно, пересобрать проект, чтобы AndroidStudio сгенерировала вспомогательные классы
  3. сделать переменную binding как поле класса Fragment
  4. инициализировать binding в onCreateView и удалять в onDestroyView
  5. получать доступ ко всем View, у которых есть id, описанным в layout файле (XML)
     через переменную binding.
  Для включения ViewBinding нужно добавить в build.gradle приложения (не проекта)
  android {
    ...
    buildFeatures {
        viewBinding true
    }
  }
  После этого, для каждого XML файла (layout) будут сгенерированы вспомогательные классы,
  которые упростят inflate UI фрагмента и будут хранить ссылки на каждый View, для которого
  есть id (в XML файле).
 */
@AndroidEntryPoint
class ArticleFragment : Fragment() {
    // Экземляр ViewBinding будет храниться в переменной _binding,
    // но для фрагмента его нужно создавать
    // в onCreateView и удалять в onDestroyView (я не делаю удаление).
    private var _binding: ArticleFragmentBinding? = null
    private val binding get() = _binding!!
    // ===
    private val viewModel: ArticleViewModel by viewModels()
    private val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Это делается вместо inflate. После этого на View (Button, TextView) можно
        // ссылаться через binding (без findViewById)
        _binding = ArticleFragmentBinding.inflate(inflater, container, false)
        return binding.root
        // ===
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.response.observe(viewLifecycleOwner){
            // вот ссылка на WebView из article_fragment.xml layout без findViewById
            // через binding
            binding.articlePage.loadDataWithBaseURL(
                    articleHtmlUrl(args.article,args.lang), it, "text/html", null, null)
        }
        viewModel.fetchJsonPage( articleJsonUrl(args.article,args.lang) )
    }
    private fun articleJsonUrl(article: String, lang: String ) : String {
        return "https://${lang}.wikipedia.org/w/api.php?action=parse&format=json&page=${article}&prop=text&format=json"
    }
    private fun articleHtmlUrl(article: String, lang: String ) : String {
        return "https://${lang}.wikipedia.org/wiki/${article}"
    }
}