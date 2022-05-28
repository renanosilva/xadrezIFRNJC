package arquitetura;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Filtro que verifica se um usuário está logado para poder ter acesso a determinadas
 * funcionalidades do sistema.
 * @author Renan	
 */
public class LoginFilter implements Filter {
	
	/** 
	 * Se existirem páginas específicas cujo acesso deva ser permitido mesmo sem estar logado,
	 * devem ser adicionadas neste array. 
	 * */
	private final String[] urlsPermitidas = {"index.xhtml", "erro.xhtml","login.xhtml","CadastrarUsuario.xhtml",
			"busca_noticia.xhtml", "busca_usuario.xhtml", "historiaequipe.xhtml", "influenciasnaregiao.xhtml",
			"membrosdaequipe.xhtml", "modelosdascamisas.xhtml", "mostra_noticia.xhtml", "perfilUsuario.xhtml",
			"busca_eventoTorneio.xhtml", "busca_treino.xhtml","mostra_depoimento.xhtml","busca_depoimento.xhtml",
			"usuarios_destaque.xhtml","busca_torneios_futuros.xhtml"};
	
	@Override
	public void destroy() {
	}

	/** 
	 * Método que irá filtrar as requisições de acesso às páginas do sistema,
	 * e que irá permitir ou bloquear o acesso, dependendo se o usuário
	 * está logado ou não. 
	 * */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		/* Obtendo requisição de acesso */
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		HttpSession session = request.getSession();
        String reqUrl = request.getRequestURI(); //Obtendo URL de acesso atual
        
        //Indica se o acesso será permitido ou não. Por padrão, não é permitido. 
   		boolean permitido = false; 

   		/* Se a URL contiver o nome "público" ou se for a URL inicial do sistema ou 
   		se for o JSF (Faces) que estiver requisitando acesso, então o acesso
   		deve ser permitido */
   		if (reqUrl.contains("publico") || reqUrl.equals("/XadrezIFRNJC/")
   				|| reqUrl.contains(".faces.")) {
   			permitido = true;
   		}
   		
   		/* Se até aqui ainda não foi permitido, deve verificar o array de URLs permitidas, para
   		 * decidir se a URL atual é permitida ou não */
   		if (!permitido){
	   		for (String url : urlsPermitidas){
	   			//Se encontrou a URL atual no array, então deve permitir
	   			if (reqUrl.contains(url)){
	   				permitido = true;
	   				break;
	   			}
	   		}
   		}

   		/* 
   		 * Se houver usuário logado ou então o acesso foi previamente permitido,
   		 * deve-se liberar o acesso
   		 */
   		
        if (session.getAttribute("usuarioLogado") != null || permitido) {
             //Libera o acesso
        	chain.doFilter(request, response);
        } else {
        	//Neste caso, não foi permitido, então redireciona para a página de login
        	response.sendRedirect("/XadrezIFRNJC/login.xhtml");
        	return;
        }
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}