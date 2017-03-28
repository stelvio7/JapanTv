package com.noh.util;

import java.lang.ref.WeakReference;                    

import com.japantv.settop1.R;




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;


/**
 * ������ AsyncTask.
 *
 * �� android.os.AsyncTask�� �ٸ���
 * - Activity�� ���� ���� ����(weak reference) ����
 * - onPreExecute���� '�۾�ó����' ���α׷��� ���̾�α� �ڵ� ����
 * - onPostExecute���� ���α׷��� ���̾�α� �ڵ� ����
 * - doInBackground���� �����߻��(throws exception) ���α׷��� ���̾�α� 
 *   �ڵ� ���� �� �����޽��� �佺Ʈ ������
 * - ���� doInBackground���� �����߻�� ���� Ŭ������ onPostExecute�� ������� ����
 * - '�۾�ó����' ���α׷��� ���̾�α׿��� ����ڰ� ���Ű ������ onCancelled ����
 * - onCancelled���� ���α׷��� ���̾�α� �ڵ� ���� �� �۾���� �޽��� ������
 *
 * - ���� : 
 * http://android-developers.blogspot.com/2010/07/multithreading-for-performance.html
 * http://tigerwoods.tistory.com/28
 *
 */
public abstract class EnhancedAsyncTask<Params, Progress, Result, WeakTarget extends Activity>
        extends AsyncTask<Params, Progress, Result> {

    protected WeakReference<WeakTarget> mTarget;
    protected Throwable mException = null;
    protected ProgressDialog mProgressDialog;

    /** �۾�ó���� ���̾�α׿� ������ �޽��� */
    protected int mTaskProcessingMessage = R.string.task_progressing;

    /** �۾���ҽ� ������ �޽��� */
    protected int mTaskCancelledMessage = R.string.task_cancelled;

    /** �۾�ó���� ���̾�α׸� �������� ���� */
    protected boolean mProgressDialogEnabled = true;

    public EnhancedAsyncTask(WeakTarget target) {
        mTarget = new WeakReference<WeakTarget>(target);
    }

    /** {@inheritDoc} */
    @Override
    protected final void onPreExecute() {
        final WeakTarget target = mTarget.get();
        if (target != null) {
            showProgress(target);
            this.onPreExecute(target);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected final Result doInBackground(Params... params) {
        final WeakTarget target = mTarget.get();
        if (target != null) {
            try {
                return this.doInBackground(target, params);
            } catch (Throwable t) {
                this.mException = t;
                return null;
            }
        } else {
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    protected final void onPostExecute(Result result) {
        final WeakTarget target = mTarget.get();
        if (target != null) {
            if (mException != null) {
                showError(target, mException);
                return;
            }

            dismissProgress();
            this.onPostExecute(target, result);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void onCancelled() {
        final WeakTarget target = mTarget.get();
        if (target != null) {
            showCancelMessage(target);
            this.onCancelled(target);
        }
    }

    /**
     * �۾��� ���̾�α� ������.
     * @param message
     */
    protected void showProgress(Context context) {
        if (mProgressDialogEnabled == false)
            return;

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                //BACK Ű ������ ���̾�α� �ݰ� ���� �۾� ���
                //NOTE: ���̾�α� ���ִ� ���¿����� BACKŰ ������ onBackPressed()�� ������� ����
                EnhancedAsyncTask.this.cancel(true);
            }
        });

        String message = context.getString(mTaskProcessingMessage);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    /**
     * �۾��� ���̾�α� ����
     */
    protected void dismissProgress() {
        if (mProgressDialogEnabled == false)
            return;

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * �۾���� �޽��� ǥ��. �۾��� ���̾�α״� �ڵ� ����.
     */
    protected void showCancelMessage(Context context) {
        dismissProgress();
        Toast.makeText(context, mTaskCancelledMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * �����޽��� ǥ��. �۾��� ���̾�α״� �ڵ� ����.
     * @param t
     */
    protected void showError(Context context, Throwable t) {
        dismissProgress();

        //TODO exception ���� ��� ������ �����޽��� ����
        String errorMessage = context.getString(R.string.str_network_error);

        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
    }

    //-------------- ���� Ŭ�������� ���� �Ǵ� �������̵��� �޼ҵ��
    /**
     * �۾� �� ó��. UI��� ó�� ����.
     * @param target
     */
    protected void onPreExecute(WeakTarget target) {
        // No default action
    }

    /**
     * ���� ��׶��� �۾�ó��.
     *
     * ���⼭ Exception�� �������� onPostExecute���� �ڵ����� �ش� Exception�� ó����.
     * ����Ŭ�������� try/catch�� ���� exception�� ��Ƽ� ���� ó������.
     *
     * @param target
     * @param params
     * @return
     */
    protected abstract Result doInBackground(WeakTarget target, Params... params);

    /**
     * �۾� �� ó��. UI��� ó�� ����.
     * @param target
     * @param result
     */
    protected void onPostExecute(WeakTarget target, Result result) {
        // No default action
    }

    /**
     * �۾� ��� ó��. UI��� ó�� ����.
     * @param target
     */
    protected void onCancelled(WeakTarget target) {
        // // No default action
    }

}