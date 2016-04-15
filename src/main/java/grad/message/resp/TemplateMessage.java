package grad.message.resp;

import java.util.Map;


/**
 * Created by Jamie on 4/15/16.
 */
public class TemplateMessage {

    /**
     * template id
     */
    private String template_id;

    /**
     * user openid
     */
    private String fromUserName;

    /**
     * Note URL
     */
    private String url;

    /**
     * title color
     */
    private String topcolor;

    /**
     * data
     */
    private Map<String, TemplateData> data;

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String touser) {
        this.fromUserName = touser;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTopcolor() {
        return topcolor;
    }

    public void setTopcolor(String topcolor) {
        this.topcolor = topcolor;
    }

    public Map<String, TemplateData> getData() {
        return data;
    }

    public void setData(Map<String, TemplateData> data) {
        this.data = data;
    }
}
