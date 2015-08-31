package daimamiao.com.myokhttp.utils;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import daimamiao.com.myokhttp.R;
import daimamiao.com.okhttp.application.App;

/**
 * Created by pengying on 2015/8/31.
 */
public class ToastUtils {

    // 是否隐藏测试Toast
    private static final boolean isShow;

    static {
        isShow = PackageUtil.getBooleanMataData("IS_DUBUG");
    }

    public static void testToast(String text) {
        if (isShow) {
            Toast.makeText(App.getAppContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    public static void testToast(int resId) {
        if (isShow) {
            Toast.makeText(App.getAppContext(), resId, Toast.LENGTH_SHORT).show();
        }
    }

    public static void toast(String text) {
        RunnableUtils.runWithTryCatch(() -> {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                Toast.makeText(App.getAppContext(), text, Toast.LENGTH_SHORT).show();
            } else {
                Looper.prepare();
                Toast.makeText(App.getAppContext(), text, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });

    }

    public static void toast(int resId) {
        Toast.makeText(App.getAppContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void toast(int resId, Object... params) {
        Context context = App.getAppContext();
        if (null != params) {
            Toast.makeText(context, context.getString(resId, params), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
        }
    }

    /*public static void showToastView(int hint) {
        Context con = App.getAppContext();
        if (null != con) {
            LayoutInflater in = LayoutInflater.from(con);
            View toastView = in.inflate(R.layout.toast_custom, null);
            final TextView localTextView = (TextView) toastView.findViewById(R.id.tv_content);
            localTextView.setText(con.getString(hint));

            Toast toast = new Toast(con);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setView(toastView);
            toast.show();
        }
    }*/

    /**
     * 吐司显示在指定控件下方
     *
     * @param v
     */
    public static void showAtLocatl(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
    }

    /**
     * 显示自定义弹出窗
     *
     * @param res
     *//*
    public static void showSuccessToast(@StringRes int res) {
        showToast(App.getStr(res), true);
    }
*/
    /**
     * 显示自定义弹出窗
     *
     * @param msg
     *//*
    public static void showSuccessToast(String msg) {
        showToast(msg, true);
    }

    public static void showColdToast(String msg) {
        showToast(msg, false);
    }

    private static void showToast(String msg, boolean isSuccess) {
        Context context = App.getAppContext();
        final Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL);
        View view = View.inflate(context, R.layout.prompt_item, null);
        SuccessTickView tickView = (SuccessTickView) view.findViewById(R.id.iv_icon);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_cold);
        tickView.setVisibility(isSuccess ? View.VISIBLE : View.GONE);
        imageView.setVisibility(!isSuccess ? View.VISIBLE : View.GONE);
        if (isSuccess) {
            tickView.startTickAnim();
        }
        TextView text = (TextView) view.findViewById(R.id.tv_text);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, isSuccess ? 13 : 15);
        text.setText(msg);
        toast.setView(view);
        toast.show();
    }

    public static void showCommentDialog(FragmentActivity activity, String score, boolean isReplyUser) {
        if (!PrefernceUtils.getBoolean(true, ConfigName.IS_FIRSTCOMMENT)) {
            new CommentSuccessDialog(activity, App.getStr(R.string.comment_post_score_jia) + score).show();
            PrefernceUtils.setBoolean(ConfigName.IS_FIRSTCOMMENT, true);
        } else {
            if (isReplyUser) {
                showSuccessToast(App.getStr(R.string.comment_post_score2));
            } else if ("0".equals(score)) {
                showSuccessToast(App.getStr(R.string.comment_post_score1));
            } else {
                showColdToast(App.getStr(R.string.comment_post_score) + score);
            }
        }
    }*/
}
