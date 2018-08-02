package hyj.xw.inputMethod;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.InputConnection;

import static android.R.attr.button;

/**
 * Created by Administrator on 2018/8/1 0001.
 */

public class AndroidInputMethodService extends InputMethodService implements
        View.OnClickListener {
    @Override
    public void onClick(View view) {

        InputConnection inputConnection = getCurrentInputConnection();
        inputConnection.commitText("666", 1);

    }
}
