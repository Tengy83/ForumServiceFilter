package telran.ashkelon2018.forum.service.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import telran.ashkelon2018.forum.configuration.AccountConfiguration;
import telran.ashkelon2018.forum.configuration.AccountUserCredentials;
import telran.ashkelon2018.forum.dao.RoleRepository;
import telran.ashkelon2018.forum.dao.UserAccountRepository;
import telran.ashkelon2018.forum.domain.UserAccount;

@Service
@Order(10)
public class AccountManagmentRolesFilter implements Filter {
	@Autowired
	UserAccountRepository repository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	AccountConfiguration configuration;
	
	@Override
	public void doFilter(ServletRequest reqs, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) reqs;
		HttpServletResponse response = (HttpServletResponse) resp;
		String path = request.getServletPath();
		String method = request.getMethod();
		String token = request.getHeader("Authorization");
		if (token != null && !path.startsWith("/account/password") && !(path.equals("/account") && method.equals("POST"))) {
			AccountUserCredentials accountUserCredentials = configuration.tokenDecode(token);
			UserAccount userAccount = repository.findById(accountUserCredentials.getLogin()).orElse(null);
			if (userAccount == null) {
				response.sendError(403,"Forbidden");
				return;
			}
			String[] uriArr = path.split("/");
			
			// AccountManagment Filter
			if (path.startsWith("/account/")) {				
				boolean moderBool = userAccount.getRoles().contains(roleRepository.findById("moderator").get());
				boolean adminBool = userAccount.getRoles().contains(roleRepository.findById("admin").get());
				boolean ownerBool = uriArr[uriArr.length - 1].equals(userAccount.getLogin());
				
				
				// Add and delete roles. Access: admin
				if (path.startsWith("/account/role") && !adminBool) {
					response.sendError(403,"Forbidden");
					return;
				}
				
				// Delete User. Access: admin, moderator, owner
				if (path.matches("/account/([A-Za-z0-9_]+)") && method.equals("DELETE")) {
					UserAccount  userDelete = repository.findById(uriArr[1]).orElse(null);
					if (userDelete == null && !adminBool && !moderBool && !ownerBool || (userDelete.getRoles().contains(roleRepository.findById("admin").get()) && !adminBool)) {
						response.sendError(403,"Forbidden");
						return;
					}
					
				}
				
				// Add and delete roles from User. Access: admin
				if (path.matches("/account/([A-Za-z0-9_]+)/([A-Za-z0-9_]+)") && (method.equals("DELETE") || method.equals("PUT")) && !adminBool) {
					response.sendError(403,"Forbidden");
					return;
				}
			}
			
		}
		chain.doFilter(request, response);
	}

}
