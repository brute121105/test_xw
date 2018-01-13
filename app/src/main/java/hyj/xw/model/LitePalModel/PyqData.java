package hyj.xw.model.LitePalModel;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by Administrator on 2018/1/10.
 */

public class PyqData extends DataSupport {
    private String text;
    private Date time;


    public PyqData(String text, Date time) {
        this.text = text;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
