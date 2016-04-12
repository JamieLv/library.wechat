package grad.pojo;

import java.util.Map;

/**
 *
 * Created by Jamie on 4/11/16.
 */
public class UpdateNoteTemplate {
    /**
     * template id
     */
    private String template_id;

    /**
     * user openid
     */
    private String touser;

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

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
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
