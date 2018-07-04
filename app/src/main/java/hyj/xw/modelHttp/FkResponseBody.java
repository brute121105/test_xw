package hyj.xw.modelHttp;

/**
 * Created by Administrator on 2018/7/3 0003.
 */

public class FkResponseBody extends ResponseBase {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    class Data{
        private int time;
        private int[] ponits;

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int[] getPonits() {
            return ponits;
        }

        public void setPonits(int[] ponits) {
            this.ponits = ponits;
        }
    }
}

