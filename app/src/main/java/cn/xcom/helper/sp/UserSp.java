package cn.xcom.helper.sp;

import android.content.Context;
import android.content.SharedPreferences;

import cn.xcom.helper.bean.UserInfo;

public class UserSp extends BaseSp<UserInfo> {

	public UserSp(Context context) {
		super(context, "user_sp");
	}

	@Override
	public void read(UserInfo user) {
		// 安全检查
		if (user == null) {
			user = new UserInfo();
		}
		if (getSP().contains("userId")) {
			user.setUserId(getSP().getString("userId", ""));
		}
		if (getSP().contains("userPhone")) {
			user.setUserPhone(getSP().getString("userPhone", ""));
		}
		if (getSP().contains("userCode")) {
			user.setUserCode(getSP().getString("userCode", ""));
		}
		if (getSP().contains("userName")) {
			user.setUserName(getSP().getString("userName", ""));
		}
		if (getSP().contains("userPassword")) {
			user.setUserPassword(getSP().getString("userPassword", ""));
		}
		if (getSP().contains("userGender")) {
			user.setUserGender(getSP().getString("userGender", ""));
		}

		if (getSP().contains("userImg")) {
			user.setUserImg(getSP().getString("userImg", ""));
		}

	}

	@Override
	public UserInfo read() {
		UserInfo result = null;
		result = new UserInfo();
		read(result);
		return result;
	}

	@Override
	public void write(UserInfo user) {
		SharedPreferences.Editor editor = getSP().edit();
		if (!user.getUserId().equals("")) {
			editor.putString("userId", user.getUserId());
		}
		if (!user.getUserPhone().equals("")) {
			editor.putString("userPhone", user.getUserPhone());
		}
		if (!user.getUserPhone().equals("")) {
			editor.putString("userCode", user.getUserCode());
		}
		if (!user.getUserName().equals("")) {
			editor.putString("userName", user.getUserName());
		}
		if (!user.getUserPassword().equals("")) {
			editor.putString("userPassword", user.getUserPassword());
		}
		if (!user.getUserGender().equals("")) {
			editor.putString("userGender", user.getUserGender());
		}
		if (!user.getUserImg().equals("")) {
			editor.putString("userImg", user.getUserImg());
		}
		editor.commit();
	}

	@Override
	public void clear() {
		SharedPreferences.Editor editor = getSP().edit();
		editor.clear();
		editor.commit();
	}
}