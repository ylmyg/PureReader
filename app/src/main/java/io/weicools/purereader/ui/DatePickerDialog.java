package io.weicools.purereader.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.weicools.purereader.R;
import java.util.Calendar;

/**
 * @author Weicools Create on 2018/4/12.
 * <p>
 * desc: 日期选择Dialog
 */
public class DatePickerDialog extends DialogFragment {
  @BindView(R.id.date_picker) DatePicker mDatePicker;
  Unbinder unbinder;

  private int currYear, currMonth, currDay;
  private OnDialogListener mOnDialogListener;
  private Calendar mMaxDate, mMinDate;

  public DatePickerDialog () { }

  public static DatePickerDialog newInstance (OnDialogListener listener, int year, int monthOfYear, int dayOfMonth,
      Calendar maxDate, Calendar minDate) {
    DatePickerDialog ret = new DatePickerDialog();
    ret.initialize(listener, year, monthOfYear, dayOfMonth, maxDate, minDate);
    return ret;
  }

  public static DatePickerDialog newInstance (OnDialogListener listener, int year, int monthOfYear, int dayOfMonth) {
    DatePickerDialog ret = new DatePickerDialog();
    ret.initialize(listener, year, monthOfYear, dayOfMonth);
    return ret;
  }

  private void initialize (OnDialogListener listener, int year, int monthOfYear, int dayOfMonth, Calendar maxDate,
      Calendar minDate) {
    mOnDialogListener = listener;

    currYear = year;
    currMonth = monthOfYear;
    currDay = dayOfMonth;
    mMaxDate = maxDate;
    mMinDate = minDate;
  }

  private void initialize (OnDialogListener listener, int year, int monthOfYear, int dayOfMonth) {
    mOnDialogListener = listener;

    currYear = year;
    currMonth = monthOfYear;
    currDay = dayOfMonth;
  }

  @Override
  public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_date_picker, container, false);
    unbinder = ButterKnife.bind(this, view);

    mDatePicker.init(currYear, currMonth, currDay, null);
    mDatePicker.setMaxDate(mMaxDate.getTimeInMillis());
    mDatePicker.setMinDate(mMinDate.getTimeInMillis());
    return view;
  }

  @OnClick(R.id.btn_ok)
  void onSelectDate () {
    mOnDialogListener.onDateSet(DatePickerDialog.this, mDatePicker.getYear(), mDatePicker.getMonth(),
        mDatePicker.getDayOfMonth());
    dismiss();
  }

  @OnClick(R.id.btn_cancel)
  void onCancel () {
    dismiss();
  }

  @OnClick(R.id.btn_look_history)
  void onLoadHository () {
    dismiss();
    mOnDialogListener.onLoadHistoryGank();
  }

  @Override
  public void onDestroyView () {
    super.onDestroyView();
    unbinder.unbind();
  }

  public interface OnDialogListener {
    /**
     * 选择日期
     *
     * @param view dialog
     * @param year 年
     * @param monthOfYear 月
     * @param dayOfMonth 日
     */
    void onDateSet (DatePickerDialog view, int year, int monthOfYear, int dayOfMonth);

    /**
     * 查看历史干货日期
     */
    void onLoadHistoryGank ();
  }
}
