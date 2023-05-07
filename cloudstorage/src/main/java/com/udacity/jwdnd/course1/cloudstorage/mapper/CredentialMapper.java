package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS")
    public List<Credential> getAllCredentials();

    @Select("SELECT * FROM CREDENTIALS where credentialid = #{credentialid}")
    public Credential getCredentialByID(Integer credentialid);

    @Insert("INSERT INTO CREDENTIALS (url, username, _key, password, userid) VALUES(#{url}, #{username}, #{_key}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int insert(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, _key = #{_key}, password = #{password}\n" +
            "WHERE credentialid = #{credentialid}")
    public int updateCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    public void deleteCredential(Integer credentialid);
}
