package com.ax.springmvc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ax.springmvc.pojo.FileInfo;
import com.ax.springmvc.pojo.Folio;

public interface FileInfoMapper {
	/**
	 * �õ�һ�����ڸ�Ŀ¼�����ɵ��ļ�
	 * 
	 * @param userId    �ĸ��û���
	 * @param parentDir ���ļ���Id
	 * @param isNotDel  �Ƿ�ɾ��
	 * @return
	 */
	List<FileInfo> selectByUserId(@Param("userId") int userId, @Param("parentDir") int parentDir,
			@Param("isNotDel") int isNotDel, @Param("isNotShare") int isNotShare);

	/**
	 * �����ļ����͵õ����û�ָ�����ļ�
	 * 
	 * @param userId    �ĸ��û�
	 * @param fileTypes �ļ�����
	 * @return
	 */
	List<FileInfo> selectByType(@Param("userId") int userId, @Param("fileTypes") String[] fileTypes,
			@Param("isNotShare") int isNotShare);

	/**
	 * �޸��ļ���Ϣ
	 * 
	 * @param fileInfo
	 * @return
	 */
	@Update("UPDATE `skydrive`.`fileinfo` SET `fileName` = #{fileName},`fileSrc` = #{fileSrc},`parentDir` = #{parentDir},`fileType` = #{fileType},`isNotDel`=#{isNotDel},`fileUpdateTime` = NOW(),`showName`=#{showName} WHERE `id` = #{id}")
	int updateFileInfo(FileInfo fileInfo);

	/**
	 * �����ļ�id�õ��ļ���Ϣ
	 * 
	 * @param id
	 * @return
	 */
	@Select("select * from fileInfo where id = #{id}")
	FileInfo selectOne(@Param("id") int id);

	/**
	 * ɾ���ļ�
	 * 
	 * @param id
	 * @return
	 */
	@Delete("delete from fileinfo where id = #{id}")
	int deleteFileInfo(@Param("id") int id);

	/**
	 * ��������ļ�
	 * 
	 * @param fileInfos
	 * @return
	 */
	int fileUpLoad(@Param("fileInfos") List<FileInfo> fileInfos);

	/**
	 * ���һ���ļ���¼
	 * 
	 * @param fileInfo
	 * @return
	 */
	int newFile(FileInfo fileInfo);

	/**
	 * �����޸��ļ�
	 */
	int updateFileInfos(@Param("fileInfos") List<FileInfo> fileInfos);

	/**
	 * �������ļ�ɾ��������վ
	 */
	int updateFileTodel(@Param("ids") List<Integer> ids, @Param("isNotDel") int isNotDel);

	/**
	 * ������ѯ�ļ�
	 */
	List<FileInfo> selectByIds(@Param("ids") List<Integer> ids);

	/**
	 * ��������ɾ���ļ�
	 */
	int deleteFiles(@Param("ids") List<Integer> ids);

	/**
	 ** Ϊ�˽���ļ�ͬ������
	 */
	@Select("select * from fileInfo where userId=#{userId} and parentDir=#{parentDir} and isNotDel=1 and isNotShare=1 and (fileName=#{fileName} or fileName=(SELECT fileName FROM fileInfo WHERE showName=#{fileName} AND userId=#{userId} and parentDir=#{parentDir} and isNotDel=1 LIMIT 0,1)) ORDER BY fileUpdateTime DESC LIMIT 0,1")
	FileInfo selectOneByFileNmae(@Param("userId") int userId, @Param("parentDir") int parentDir,
			@Param("fileName") String fileName);

	/**
	 ** ģ����ѯ�ļ�
	 * 
	 * @param userId
	 * @param fileName
	 * @return
	 */
	@Select("select * from fileInfo where userId = #{userId} and showName like CONCAT('%',#{fileName,jdbcType = VARCHAR},'%') and isNotDel=1 and isNotShare=1")
	List<FileInfo> listByLikeFileName(@Param("userId") int userId, @Param("fileName") String fileName);
}
