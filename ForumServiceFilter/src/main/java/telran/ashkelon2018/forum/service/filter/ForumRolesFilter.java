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
import telran.ashkelon2018.forum.dao.ForumRepository;
import telran.ashkelon2018.forum.dao.RoleRepository;
import telran.ashkelon2018.forum.dao.UserAccountRepository;
import telran.ashkelon2018.forum.domain.Comment;
import telran.ashkelon2018.forum.domain.Post;
import telran.ashkelon2018.forum.domain.UserAccount;

@Service
@Order(15)
public class ForumRolesFilter implements Filter {
	@Autowired
	UserAccountRepository repository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	AccountConfiguration configuration;
	
	@Autowired
	ForumRepository forumRepository;
	
	@Override
	public void doFilter(ServletRequest reqs, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) reqs;
		HttpServletResponse response = (HttpServletResponse) resp;
		String path = request.getServletPath();
		String method = request.getMethod();
		String token = request.getHeader("Authorization");
		if (token != null && !(path.equals("/forum/post") && method.equals("POST")) && !(path.startsWith("/forum/post") && method.equals("GET"))) {
			AccountUserCredentials accountUserCredentials = configuration.tokenDecode(token);
			UserAccount userAccount = repository.findById(accountUserCredentials.getLogin()).orElse(null);
			String[] uriArr = path.split("/");
		
			// Forum Filter
			if (path.startsWith("/forum/")) {
				boolean moderBool = userAccount.getRoles().contains(roleRepository.findById("moderator").get());
				boolean adminBool = userAccount.getRoles().contains(roleRepository.findById("admin").get());
				
				// Update Post and Add Tag. Access: owner
				if((path.matches("/forum/post/([A-Za-z0-9_]+)") && method.equals("PUT")) || (path.matches("/forum/post/([A-Za-z0-9_]+)/tag/([A-Za-z0-9_]+)") && method.equals("POST"))) {
					Post post = forumRepository.findById(uriArr[3]).orElse(null);
					if (post != null && !post.getAuthor().equals(userAccount.getLogin())) {
						response.sendError(403,"Forbidden");
						return;
					}
				}
				
				// Delete Post and Delete Tag. Access: admin, moderator, owner
				if((path.matches("/forum/post/([A-Za-z0-9_]+)") && method.equals("DELETE")) || (path.matches("/forum/post/([A-Za-z0-9_]+)/tag/([A-Za-z0-9_]+)") && method.equals("DELETE"))) {
					Post post = forumRepository.findById(uriArr[3]).orElse(null);
					if (post != null && !moderBool && !adminBool && !post.getAuthor().equals(userAccount.getLogin())) {
						response.sendError(403,"Forbidden");
						return;
					}
				}
				
				// Add Like to Post and Add Comment to Post. Access: all but post owner
				if ((path.matches("/forum/post/([A-Za-z0-9_]+)/like") || path.matches("/forum/post/([A-Za-z0-9_]+)/comment") ) && method.equals("POST")) {
					Post post = forumRepository.findById(uriArr[3]).orElse(null);
					if (post != null && post.getAuthor().equals(userAccount.getLogin())) {
						response.sendError(403,"Forbidden");
						return;
					}
				}
				
				// Delete Comment. Access: admin, moderator, comment owner
				if (path.matches("/forum/post/([A-Za-z0-9_]+)/comment/([A-Za-z0-9_]+)") && method.equals("DELETE")) {
					Post post = forumRepository.findById(uriArr[3]).orElse(null);
					Comment comment = post.getComments().stream().filter(c->c.getId().equals(uriArr[5])).findFirst().orElse(null);
					if (comment != null && !moderBool && !adminBool && !comment.getUser().equals(userAccount.getLogin())) {
						response.sendError(403,"Forbidden");
						return;
					}
				}
				
				// Update Comment. Access: comment owner
				if (path.matches("/forum/post/([A-Za-z0-9_]+)/comment/([A-Za-z0-9_]+)") && method.equals("PUT")) {
					Post post = forumRepository.findById(uriArr[3]).orElse(null);
					Comment comment = post.getComments().stream().filter(c->c.getId().equals(uriArr[5])).findFirst().orElse(null);
					if (comment != null && !comment.getUser().equals(userAccount.getLogin())) {
						response.sendError(403,"Forbidden");
						return;
					}
				}
				
				// Add Like to Comment. Access: all but comment owner
				if (path.matches("/forum/post/([A-Za-z0-9_]+)/comment/([A-Za-z0-9_]+)/like") && method.equals("POST")) {
					Post post = forumRepository.findById(uriArr[3]).orElse(null);
					Comment comment = post.getComments().stream().filter(c->c.getId().equals(uriArr[5])).findFirst().orElse(null);
					if (comment != null && comment.getUser().equals(userAccount.getLogin())) {
						response.sendError(403,"Forbidden");
						return;
					}
				}
			}
			
		}
		chain.doFilter(request, response);

	}

}
