package jin.spring.member.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jin.spring.member.dto.MemberDTO;

@Repository
public class memberDAOImp implements MemberDAO{
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public void register(MemberDTO memberDTO) throws Exception {
		sqlSessionTemplate.insert("jin.spring.member.register", memberDTO);
	}

}
