package com.gsy.ml.ui.home;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivitySiteLayoutBinding;
import com.gsy.ml.model.common.AddressModel;
import com.gsy.ml.model.common.EditAddressModel;
import com.gsy.ml.ui.common.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ml.gsy.com.library.utils.runtimepermission.PermissionsManager;
import ml.gsy.com.library.utils.runtimepermission.PermissionsResultAction;

/**
 * Created by Administrator on 2017/5/11.
 */

public class SiteActivity extends BaseActivity implements View.OnClickListener {
    private ActivitySiteLayoutBinding mBinding;

    @Override
    public int getLayoutId() {
        return R.layout.activity_site_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
    }

    private AddressModel mAddressModel;

    private boolean is_edit = false;

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivitySiteLayoutBinding) vdb;
        mAddressModel = (AddressModel) getIntent().getParcelableExtra("address");
        is_edit = getIntent().getBooleanExtra("is_edit", false);
        if (mAddressModel == null) {
            mAddressModel = new AddressModel();
            Toast.makeText(aty, "地址信息错误！", Toast.LENGTH_SHORT).show();
        }
        initView();
        mBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });
    }

    private void checkData() {
        String add_info = mBinding.etAddInfo.getText().toString().trim();
        String name = mBinding.etName.getText().toString().trim();
        String phone = mBinding.etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(add_info)) {
            Toast.makeText(aty, "请输入你的门牌号信息!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mAddressModel.getType() != -1) {
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(aty, "联系人不能为空!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(aty, "联系号码不能为空!", Toast.LENGTH_SHORT).show();
                return;
            }
            Pattern pattern = Pattern.compile("^(0\\d{2,3}\\d{7,8}(\\d{3,5}){0,1})|(((13[0-9])|(14([4,7]))|(15([0-9]))|(17([0-1,3,5-8]))|(18[0-9]))\\d{8})$");
            Matcher matcher = pattern.matcher(phone);
            boolean a = matcher.matches();
            if (!a) {
                Toast.makeText(aty, "请输入正确的号码!" + a, Toast.LENGTH_SHORT).show();
                return;
            }
            mAddressModel.setAdd_name(name);
            mAddressModel.setAdd_phone(phone);
        }
        mAddressModel.setDoor_info(add_info);
        if (is_edit) {
            EditAddressModel editAddressModel = new EditAddressModel();
            editAddressModel.setmAddress(mAddressModel);
            EventBus.getDefault().post(editAddressModel);
        } else {
            EventBus.getDefault().post(mAddressModel);
        }


        finish();
    }

    private void initView() {
        if (mAddressModel.getType() == -1) {
            mBinding.llyInfo.setVisibility(View.GONE);
        } else {
            mBinding.llyInfo.setVisibility(View.VISIBLE);

            if (mAddressModel.getType() == 1 || mAddressModel.getType() == 10) {
                mBinding.etName.setHint("寄件人");
            } else if (mAddressModel.getType() == 3) {
                mBinding.etName.setHint("联系人");
            } else {
                mBinding.etName.setHint("收件人");
            }
        }
        String name = mAddressModel.getName();
        String address = TextUtils.isEmpty(mAddressModel.getAddress()) ? "" : "(" + mAddressModel.getAddress() + ")";
        mBinding.etAddress.setText(name + address);
        mBinding.ilHead.tvTitle.setText("具体信息");
        mBinding.btnOk.setOnClickListener(this);
        mBinding.ilHead.llyLeft.setOnClickListener(this);
        mBinding.imgLian.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.btn_ok:
                finish();
                break;
            case R.id.img_lian:
                PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(aty,
                        new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_CONTACTS},
                        new PermissionsResultAction() {
                            @Override
                            public void onGranted() {
                                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 1);
                            }

                            @Override
                            public void onDenied(String permission) {
                                Toast.makeText(MaiLiApplication.appliactionContext, "还没有开启联系人权限呢!", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAddress(AddressModel addressModel) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // ContentProvider展示数据类似一个单个数据库表
            // ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
            ContentResolver reContentResolverol = getContentResolver();
            // URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
            Uri contactData = data.getData();
            // 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
            Cursor cursor = managedQuery(contactData, null, null, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                // 条件为联系人ID
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
                Cursor phone = reContentResolverol.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                + contactId, null, null);
                while (phone.moveToNext()) {
                    String usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    mBinding.etPhone.setText(usernumber);
                }
            }
        }
    }
}
