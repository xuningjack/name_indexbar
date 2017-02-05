package com.example.sortlistview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.adapter.SortAdapter;
import com.example.utils.CharacterParser;
import com.example.utils.PinyinComparator;
import com.example.voo.SortModel;
import com.example.widget.ClearEditText;
import com.example.widget.SideBar;
import com.example.widget.SideBar.OnTouchingLetterChangedListener;

public class MainActivity extends Activity {

	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	/** 汉字转换成拼音的类 */
	private CharacterParser characterParser;
	private List<SortModel> sourceDataList;
	/** 根据拼音来排列ListView里面的数据类 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
	}

	private void initViews() {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}
			}
		});

		//单击名称列表后toast提示
		sortListView = (ListView) findViewById(R.id.name_listview);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Toast.makeText(getApplication(),
						((SortModel) adapter.getItem(position)).getName(),
						Toast.LENGTH_SHORT).show();
			}
		});
		sourceDataList = filledData(getResources().getStringArray(R.array.data));
		// 根据a-z进行排序源数据
		Collections.sort(sourceDataList, pinyinComparator);
		adapter = new SortAdapter(this, sourceDataList);
		sortListView.setAdapter(adapter);

		//查询框设置监听
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 为ListView填充数据
	 * @param data
	 * @return
	 */
	private List<SortModel> filledData(String[] data) {
		
		List<SortModel> mSortList = new ArrayList<SortModel>();
		for (int i = 0; i < data.length; i++) {
			
			SortModel sortModel = new SortModel();
			sortModel.setName(data[i]);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(data[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}
			mSortList.add(sortModel);
		}
		return mSortList;
	}

	/**
	 * 根据输入框中的值来过滤数据源，截取含有查询字符串的数据
	 * @param filterStr 查询字符串
	 */
	private void filterData(String filterStr) {
		
		List<SortModel> filterDataList = new ArrayList<SortModel>();
		if (TextUtils.isEmpty(filterStr)) {
			
			filterDataList = sourceDataList;
		} else {
			
			filterDataList.clear();
			for (SortModel sortModel : sourceDataList) {
				
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
					
					filterDataList.add(sortModel);
				}
			}
		}
		// 根据a-z进行排序
		Collections.sort(filterDataList, pinyinComparator);
		adapter.updateListView(filterDataList);
	}
}

