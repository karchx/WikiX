package github.karchx.wiki.tools.news_engine;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

public class NewsEngine {

    int tokenIndex = 0;

    TokenManager tokenManager = new TokenManager();
    NewsApiClient newsApiClient = new NewsApiClient(tokenManager.getToken(tokenIndex));

    public ArrayList<NewsArticle> getNews(String userLang) {
        ArrayList<NewsArticle> newsArticles = new ArrayList<>();

        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language(userLang)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        List<Article> responseArray = response.getArticles();
                        for (Article article : responseArray) {
                            newsArticles.add(new NewsArticle(
                                    article.getTitle(),
                                    article.getDescription(),
                                    article.getPublishedAt(),
                                    article.getUrlToImage(),
                                    article.getUrl()));
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );

        return newsArticles;
    }

}
