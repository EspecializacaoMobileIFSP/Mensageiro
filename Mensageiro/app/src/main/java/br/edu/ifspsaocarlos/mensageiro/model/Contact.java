package br.edu.ifspsaocarlos.mensageiro.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author maiko.trindade
 * @since 19/06/2016
 */
public class Contact extends RealmObject implements Parcelable {

    @SerializedName("id")
    @Expose
    @PrimaryKey
    private long id;

    @SerializedName("nome_completo")
    @Expose
    private String fullName;

    @SerializedName("apelido")
    @Expose
    private String nickName;

    public Contact() {
    }

    public Contact(String fullName, String nickName) {
        this.fullName = fullName;
        this.nickName = nickName;
    }

    public Contact(long id, String fullName, String nickName) {
        this.id = id;
        this.fullName = fullName;
        this.nickName = nickName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.fullName);
        dest.writeString(this.nickName);
    }

    protected Contact(Parcel in) {
        this.id = in.readLong();
        this.fullName = in.readString();
        this.nickName = in.readString();
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
