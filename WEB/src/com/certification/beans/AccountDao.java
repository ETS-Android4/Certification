package com.certification.beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


public class AccountDao implements Dao<AccountVo>{
	private Connection con;
	private PreparedStatement ps;
	private ResultSet rs;
	private static AccountDao instance; 
	private AccountDao() {
	}
	public static AccountDao getInstance() {
		if(instance == null) {
			instance = new AccountDao();
		}
		return instance;
	}
	
	public String checkAccount(AccountVo vo) {
		String sql = "SELECT nickname FROM account WHERE id = ? AND password = ?";
		String nickname = null;
		try {
			con = openConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, vo.getId());
			ps.setString(2, vo.getPassword());
			
			rs = ps.executeQuery();
			System.out.println(vo.getId());
			if(rs.next()) {
				nickname = rs.getString("nickname");
				System.out.println(nickname);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
		return nickname;
	}
	
	public boolean insert(AccountVo vo) {
		String sql = "INSERT INTO account VALUES(acc_seq.NEXTVAL,?,?,?,?,?,?,?,SYSDATE,?)";
		boolean result = false;
		try {
			con = openConnection();
			ps = con.prepareStatement(sql);
			
			// ����ǥ ä���
			ps.setString(1, vo.getId());
			ps.setString(2, vo.getNickname());
			ps.setString(3, vo.getEmail1());
			ps.setString(4, vo.getEmail2());
			ps.setString(5, vo.getAddress1());
			ps.setString(6, vo.getAddress2());
			ps.setString(7, vo.getAddress3());
			ps.setString(8, vo.getPassword());
			
			// ���� ���� + ��� �ޱ�
			// �� �߰��� �Ϸ� �Ǿ��ٸ� result = true
			result = ps.executeUpdate() == 1;
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
		return result;
	}
	
	
	/**
	 * ȸ�� ��ȣ�� ������ �ش� ȸ�� ����
	 * @param ȸ�� ��ȣ
	 * @return ���� ������ true, ���д� false
	 */
	public boolean delete(int num) {
		boolean result = false;
		String sql = "DELETE FROM account WHERE num = ?";
		try {
			con = openConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, num);
			result = ps.executeUpdate() == 1;
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
		return result;
	}
	
	/**
	 * vo �� ������� id, password �� ������ DB ���ڵ� ����
	 * @param ������ ���ڵ��� id, password�� �ִ� AccountVo
	 * @return ���� ������ true, ���� ���и� false
	 */
	@Override
	public boolean delete(AccountVo vo) {
		boolean result = false;
		String sql = "DELETE FROM account WHERE id = ? AND password = ?";
		try {
			con = openConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, vo.getId());
			ps.setString(2, vo.getPassword());
			result = ps.executeUpdate() == 1;
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
		return result;
	}
	
	@Override
	public boolean update(AccountVo vo) {
		String sql = "UPDATE account "
				+ "SET nickname=?, password=?, email1=?, email2=?, "
				+ "address1=?, address2=?, address3=? "
				+ "WHERE num = ?";
		boolean result = false;
		try {
			con = openConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, vo.getNickname());
			ps.setString(2, vo.getPassword());
			ps.setString(3, vo.getEmail1());
			ps.setString(4, vo.getEmail2());
			ps.setString(5, vo.getAddress1());
			ps.setString(6, vo.getAddress2());
			ps.setString(7, vo.getAddress3());
			ps.setInt(8, vo.getNum());
			result = ps.executeUpdate() == 1;
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
		return result;
	}
	
	/**
	 * num�� ������ ��� ȸ���� ������ ����ִ� vo�� ����
	 * @param ȸ����ȣ
	 * @return AccountVo
	 */
	@Override
	public AccountVo select(int num) {
		String sql = "SELECT * FROM account WHERE num = ?";
		AccountVo vo = null;
		
		try {
			con = openConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, num);
			rs = ps.executeQuery();
			if(rs.next()) {
				vo = new AccountVo();
				vo.setNum( rs.getInt("num") );
				vo.setId( rs.getString("id") );
				vo.setNickname( rs.getString("nickname") );
				vo.setEmail1( rs.getString("email1") );
				vo.setEmail2( rs.getString("email2") );
				vo.setAddress1( rs.getString("address1") );
				vo.setAddress2( rs.getString("address2") );
				vo.setAddress3( rs.getString("address3") );
				vo.setRegdate( rs.getString("regdate") );
				vo.setPassword( rs.getString("password") );
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
		return vo;
	}
	
	/**
	 * ȸ���� ID�� ������ ȸ����ȣ�� ����
	 * @param id
	 * @return ȸ�� ��ȣ(num)
	 */
	public int getAccountNum(String id) {
		String sql = "SELECT num FROM account WHERE id = ?";
		int num = 0;
		try {
			con = openConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if(rs.next()) {
				num = rs.getInt("num");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
		return num;
	}
	
	@Override
	public ArrayList<AccountVo> selectList() {
		String sql = "SELECT num, id, nickname, regdate, email1, email2 FROM account";
		
		// 1. ��� ȸ������ ����(AccountVo��)�� ���� ArrayList ��ü ����
		ArrayList<AccountVo> list = new ArrayList<>(); 
				
		try {
			
			// 2. DB ����
			con = openConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			// 3. while������ �� �� �� ��(���ڵ�) vo�� ����
			while(rs.next()) {
				AccountVo vo = new AccountVo();
				vo.setNum( rs.getInt("num") );
				vo.setId( rs.getString("id") );
				vo.setNickname( rs.getString("nickname") );
				vo.setEmail1( rs.getString("email1") );
				vo.setEmail2( rs.getString("email2") );
				vo.setRegdate( rs.getString("regdate") );
				
				// 4. ����Ʈ�� vo ���
				list.add(vo);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
		// 5. ����Ʈ�� ��°�� return
 		return list;
	}
}
















