package grad.pojo;

/**
 * Created by Jamie on 4/28/16.
 */
public class MatchRule {
    int tag_id;
    String language;

    public MatchRule(int tag_id, String language) {
        this.tag_id = tag_id;
        this.language = language;
    }

    public MatchRule(String language){
        this.language = language;
    }

    public MatchRule(int tag_id) {
        this.tag_id = tag_id;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
