package dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import dominio.EventoTorneio;
import dominio.TipoUsuario;
import dominio.Usuario;
import uteis.MetodosUteis;
import uteis.ValidadorUtil;

/**
 * DAO (Data Access Object - Objeto de Acesso a Dados) com m�todos relativos a
 * entidade {@link Usuario}.
 */
public class UsuarioDAO extends DAOGenerico {

	/** Encontrar usu�rio por login e senha. */
	public Usuario findUsuarioByLoginSenha(String login, String senha) {
		EntityManager em = getEntityManager();

		String hql = "SELECT usuario ";
		hql += "FROM Usuario usuario WHERE " + "usuario.email = :login and usuario.senha = :senha";

		Query q = em.createQuery(hql);
		q.setParameter("login", login);
		q.setParameter("senha", senha);

		try {
			Usuario usuario = (Usuario) q.getSingleResult();
			return usuario;
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * M�todo que permite listar usu�rios atrav�s da busca por diversos
	 * atributos.
	 */
	public List<Usuario> findUsuarioGeral(String nome) {
		EntityManager em = getEntityManager();

		String hql = "SELECT u ";
		hql += " FROM Usuario u WHERE u.ativo = true";

		if (ValidadorUtil.isNotEmpty(nome)) {
			hql += " AND upper(u.nome) like :nome ";
		}

		hql += " ORDER BY u.nome ASC ";

		Query q = em.createQuery(hql);

		if (ValidadorUtil.isNotEmpty(nome)) {
			q.setParameter("nome", "%" + nome.toUpperCase() + "%");
		}

		try {
			@SuppressWarnings("unchecked")
			List<Usuario> result = q.getResultList();

			return result;
		} catch (NoResultException e) {
			return null;
		}
	}

	/** Encontrar usu�rio por email **/

	public Usuario findUsuarioByLogin(String login) {
		EntityManager em = getEntityManager();

		String hql = "SELECT usuario ";
		hql += "FROM Usuario usuario WHERE " + "usuario.email = :login";

		Query q = em.createQuery(hql);
		q.setParameter("login", login);

		try {
			Usuario usuario = (Usuario) q.getSingleResult();
			return usuario;
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Usuario> buscarPeloTipo(String tipo) {
		EntityManager em = getEntityManager();

		String hql = "SELECT usuario ";
		hql += "FROM Usuario usuario WHERE " + "usuario.tipoUsuario = :tipo";

		Query q = em.createQuery(hql);
		q.setParameter("tipo", tipo);

		try {
			List<Usuario> usuarios = q.getResultList();
			return usuarios;
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Usuario> buscarUsuario(String matricula, String nome, String ordenarPor) {
		EntityManager gerenciador = getEntityManager();

		String hql = "SELECT u FROM Usuario u WHERE 1=1 ";
		hql += " AND u.ativo = true ";

		if (!MetodosUteis.estaVazia(matricula)) {
			hql += " AND u.matricula = :matricula ";
		}
		if (!MetodosUteis.estaVazia(nome)) {
			hql += " AND upper(u.nome) like :nome ";
		}
		if (!MetodosUteis.estaVazia(ordenarPor)) {
			hql += " ORDER BY :ordenarPor ";
		}

		Query q = gerenciador.createQuery(hql);

		if (!MetodosUteis.estaVazia(matricula)) {
			q.setParameter("matricula", matricula);
		}
		if (!MetodosUteis.estaVazia(nome)) {
			q.setParameter("nome", nome.toUpperCase());
		}
		if (!MetodosUteis.estaVazia(ordenarPor)) {
			q.setParameter("ordenarPor", ordenarPor);
		}

		try {
			List<Usuario> resultado = q.getResultList();
			return resultado;
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Usuario> buscarUsuario() {
		EntityManager em = getEntityManager();

		String hql = "SELECT u FROM Usuario u";
		hql += " ORDER BY u.nome ASC ";

		Query q = em.createQuery(hql);

		try {
			@SuppressWarnings("unchecked")
			List<Usuario> result = q.getResultList();

			return result;
		} catch (NoResultException e) {
			return null;
		}
	}
	// criando busca por matricula para logar como usuario API

	public Usuario findUsuarioByMatricula(String matricula) {
		EntityManager em = getEntityManager();

		String hql = "SELECT usuario ";
		hql += "FROM Usuario usuario WHERE " + "usuario.matricula like :matricula";

		Query q = em.createQuery(hql);
		q.setParameter("matricula", matricula);

		try {
			Usuario usuario = (Usuario) q.getSingleResult();
			System.out.println(usuario.getNome());
			return usuario;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Usuario findUsuarioByMatriculaSenha(String matricula, String senha) {
		EntityManager em = getEntityManager();

		String hql = "SELECT usuario ";
		hql += "FROM Usuario usuario WHERE " + "usuario.matricula like :matricula and usuario.senha = :senha";

		Query q = em.createQuery(hql);
		q.setParameter("matricula", matricula);
		q.setParameter("senha", senha);

		try {
			Usuario usuario = (Usuario) q.getSingleResult();
			return usuario;
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	public List<Usuario> buscarUsuariosFiltro() {
		EntityManager em = getEntityManager();

		String hql = "SELECT u FROM Usuario u";
		hql += " ORDER BY u.nome ASC ";

		Query q = em.createQuery(hql);

		try {
			@SuppressWarnings("unchecked")
			List<Usuario> result = q.getResultList();

			return result;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Usuario>buscarDestaques(){
		EntityManager em = getEntityManager();

		String hql = "SELECT u FROM Usuario u";
		hql += " where u.destaque = true ";

		Query q = em.createQuery(hql);

		try {
			@SuppressWarnings("unchecked")
			List<Usuario> result = q.getResultList();

			return result;
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Usuario> buscarUsuarios(String nome, TipoUsuario tipoUsuario, String cpf, String matricula, boolean ativo,boolean ordeBy) {
		EntityManager gerenciador = getEntityManager();
		

		String hql = "SELECT u FROM Usuario u WHERE 1=1 ";

		if (!MetodosUteis.estaVazia(nome)) {
			hql += " AND upper(u.nome)like :nome";
		}

		if (!MetodosUteis.estaVazia(tipoUsuario)) {
			hql += " AND u.tipoUsuario=:tipoUsuario ";
		}

		if (!MetodosUteis.estaVazia(cpf)) {
			hql += " AND u.cpf =:cpf";
		}

		if (!MetodosUteis.estaVazia(matricula)) {
			hql += " AND u.matricula like:matricula";
		}

		if (ativo) {
			hql += " AND u.ativo=:ativo";
		}
		
		if(ordeBy) {
			hql += " ORDER BY u.rating desc";
		}

		// set parametes query

		Query q = gerenciador.createQuery(hql);

		if (!MetodosUteis.estaVazia(nome)) {
			q.setParameter("nome", nome.toUpperCase() + "%");
		}

		if (!MetodosUteis.estaVazia(tipoUsuario)) {
			q.setParameter("tipoUsuario", tipoUsuario);
		}
		
		if (!MetodosUteis.estaVazia(cpf)) {
			q.setParameter("cpf",cpf);
		}

		if (!MetodosUteis.estaVazia(matricula)) {
			q.setParameter("matricula",matricula+"%");
		}
		
		if (ativo) {
			q.setParameter("ativo", ativo);
		}
		
		
		
		
		

		try {
			List<Usuario> resultado = q.getResultList();
			return resultado;
		} catch (NoResultException e) {
			return null;
		}
	}

}