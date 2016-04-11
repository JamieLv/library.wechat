/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package grad.message.resp;

import java.util.List;

/**
 *news消息（服务器回应）
 * @author smile
 */
public class NewsMessage extends BaseMessage {
    
        // 图文消息个数，限制为10条以内  
    private int ArticleCount;  
    // 多条图文消息信息，默认第一个item为大图  
    private List<Article> Articles; 

    public void setArticleCount(int ArticleCount) {
        this.ArticleCount = ArticleCount;
    }

    public void setArticles(List<Article> Articles) {
        this.Articles = Articles;
    }

    public int getArticleCount() {
        return ArticleCount;
    }

    public List<Article> getArticles() {
        return Articles;
    }
    
    
    
}
