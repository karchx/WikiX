package github.karchx.wiki.tools.news_engine;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

public class NewsEngine {

    int tokenIndex = 1;

    TokenManager tokenManager = new TokenManager();
    NewsApiClient newsApiClient = new NewsApiClient(tokenManager.getToken(tokenIndex));

    public void getNews(MutableLiveData<ArrayList<NewsArticleItem>> newsArticles, String userLang) {
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language(userLang)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        ArrayList<NewsArticleItem> articles = new ArrayList<>();
                        List<Article> responseArray = response.getArticles();
                        for (Article article : responseArray) {
                            // In some responses, there isn't url to image
                            if (article.getUrlToImage() == null) {
                                articles.add(new NewsArticleItem(
                                        article.getTitle(),
                                        article.getDescription(),
                                        article.getPublishedAt(),
                                        "",
                                        article.getUrl()));
                            } else {
                                articles.add(new NewsArticleItem(
                                        article.getTitle(),
                                        article.getDescription(),
                                        article.getPublishedAt(),
                                        article.getUrlToImage(),
                                        article.getUrl()));
                            }
                        }

                        newsArticles.postValue(new ArrayList<>(articles));
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );
    }
}
