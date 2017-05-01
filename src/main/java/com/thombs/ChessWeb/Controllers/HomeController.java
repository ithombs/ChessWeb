package com.thombs.ChessWeb.Controllers;

import java.security.Principal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thombs.ChessWeb.Models.ChessGame;
import com.thombs.ChessWeb.Models.ChessGameService;
import com.thombs.ChessWeb.Models.ChessMove;
import com.thombs.ChessWeb.Models.ChessUser;
import com.thombs.ChessWeb.Models.Role;
import com.thombs.ChessWeb.Models.User;
import com.thombs.ChessWeb.Models.UserService;
import com.thombs.ChessWeb.Models.UserValidator;



/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private UserValidator userValidator;
	
	@Autowired
	private ChessGameService chessService;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		//User u = userService.getUser("it");
		List<User> users = userService.getAllUsers();
		model.addAttribute("serverTime", formattedDate );
		model.addAttribute("users", users);
		
		//logger.info(passwordEncoder.encode("testing"));
		
		return "home";
	}
	
	@RequestMapping("/getGame")
	@ResponseBody
	public String getGame(){
		ChessGame game = chessService.getChessGameByID(5);
		return game.getGameDate().toString();
	}
	
	@RequestMapping("/makeGame")
	@ResponseBody
	public String makeChessGameTest(){
		ChessGame game = new ChessGame();
		game.setGameDate(new Timestamp(System.currentTimeMillis()));
		game.setPlayerBlack(14);
		game.setPlayerWhite(15);
		game.setWinner(15);
		
		ChessMove m1 = new ChessMove();
		ChessMove m2 = new ChessMove();
		ChessMove m3 = new ChessMove();
		List<ChessMove> moves = new ArrayList<ChessMove>();
		m1.setMove(new JSONObject().put("pieceID", 12).put("chessCommand", "move").put("row", 4).put("col", 2).toString());
		m1.setMoveNum(1);
		m2.setMove(new JSONObject().put("pieceID", 5).put("chessCommand", "move").put("row", 2).put("col", 8).toString());
		m2.setMoveNum(2);
		m3.setMove(new JSONObject().put("pieceID", 7).put("chessCommand", "move").put("row", 3).put("col", 5).toString());
		m3.setMoveNum(3);
		moves.add(m1);
		moves.add(m2);
		moves.add(m3);
		game.setMoves(moves);
		
		chessService.saveChessGame(game);
		
		
		return "Game created!";
	}
	
	@RequestMapping("/login")
	public String login(HttpServletRequest request){
		logger.info("Login page hit");
		
		String referer = request.getHeader("Referer");
		if(referer == null){
			referer = "/";
		}
		
	    return "redirect:"+ referer;
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response){
		logger.info("Logout page hit");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		new SecurityContextLogoutHandler().logout(request, response, auth);
		String referer = request.getHeader("Referer");
	    return "redirect:"+ referer;
	}
	
	@RequestMapping("/playChess")
	public String playChess(){
		return "basicChess";
	}
	
	@RequestMapping("/chessReplays")
	public String chessReplay(){
		
		return "chessReplay";
	}
	
	@RequestMapping("/profile")
	public String userProfile(Model model, Principal principal){
		User user = getCurrentUser(principal);
		model.addAttribute("username", user.getUsername());
		return "chessProfile";
	}
	
	@RequestMapping(value = "/404")
	@ResponseBody
	public String errors()
	{
		return "404";
	}
	
	@RequestMapping(value = "/createUser", method = RequestMethod.POST)
	public String userCreation(@ModelAttribute("user") User user, BindingResult result, Model model, HttpServletRequest request){
		userValidator.validate(user, result);
		List<Role> roles = new ArrayList<Role>();
		roles.add(Role.USER);
		user.setRoles(roles);
		
		if(result.hasErrors()){
			return "userCreation";
		}
		userService.saveUser(user);
		
		//Login the new user
		request.getSession();
		User newUser = userService.getUser(user.getUsername());
        Authentication authenticatedUser = new UsernamePasswordAuthenticationToken(newUser, null, newUser.getAuthRoles());
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/createUser", method = RequestMethod.GET)
	public String userCreation(Model model){
		model.addAttribute("user", new User());
		
		return "userCreation";
	}
	
	//TODO: Must be a better way of doing this (maybe make an interface and implement it within User and ChessUser)
	private User getCurrentUser(Principal principal){
		Object u = ((Authentication) principal).getPrincipal();
		if(u instanceof ChessUser){
			return ((ChessUser) u).getUser();
		}else{
			return ((User)u);
		}
	}
	
	private String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 
        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }
}
