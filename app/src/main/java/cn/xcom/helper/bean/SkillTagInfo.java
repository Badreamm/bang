package cn.xcom.helper.bean;

import java.io.Serializable;

/**
 * Created by zhuchongkun on 16/6/23.
 */
public class SkillTagInfo implements Serializable {
    private static final long serialVersionUID = 3L;
    private String skill_id;
    private String skill_name;
    private String skill_photo;
    private String skill_parent;
    private boolean checked;

    public SkillTagInfo() {
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(String skill_id) {
        this.skill_id = skill_id;
    }

    public String getSkill_name() {
        return skill_name;
    }

    public void setSkill_name(String skill_name) {
        this.skill_name = skill_name;
    }

    public String getSkill_photo() {
        return skill_photo;
    }

    public void setSkill_photo(String skill_photo) {
        this.skill_photo = skill_photo;
    }

    public String getSkill_parent() {
        return skill_parent;
    }

    public void setSkill_parent(String skill_parent) {
        this.skill_parent = skill_parent;
    }
}
