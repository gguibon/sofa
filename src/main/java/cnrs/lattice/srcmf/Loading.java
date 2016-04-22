package cnrs.lattice.srcmf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.AsyncContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet(name = "Loading", urlPatterns = {
		"/loading" }, initParams = @WebInitParam(name = "chemin", value = Params.dir) )
@MultipartConfig(location = Params.dir, fileSizeThreshold = 1048576, maxFileSize = 10485760, maxRequestSize = 52428800)
public class Loading extends HttpServlet {
	public static final String VUE = "/jsp/loading.jsp";
	public static final String CHAMP_DESCRIPTION_TRAIN = "descriptionTrain";
	public static final String CHAMP_FICHIER_TRAIN = "fichierTrain";
	public static final String CHAMP_DESCRIPTION_TEST = "descriptionTest";
	public static final String CHAMP_FICHIER_TEST = "fichierTest";
	public static final String CHEMIN = "chemin";
	public static final int TAILLE_TAMPON = 10240; // 10 ko

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Affichage de la page d'envoi de fichiers */
		String chemin = this.getServletConfig().getInitParameter(CHEMIN);
		String action = request.getParameter("action");
		
		switch(action){
		case "ooo":
			try {
				apiLaunch(chemin, request, response);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case "1on1":
			this.getServletContext().getRequestDispatcher("/jsp/result.jsp").forward(request, response);
			break;
		}
		
		
//		String mode = request.getParameter("mode");
//		if(mode.equals("start")){
//			try {
//				apiLaunch(chemin, request, response);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	
//		this.getServletContext().getRequestDispatcher("/jsp/button-test.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		this.getServletContext().getRequestDispatcher("/jsp/button-test.jsp").forward(request, response);
	}

	private void apiLaunch(String chemin, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String[] arguments = { "1on1", "-test", chemin + "lapidaire.conll", 
				"-matemodel", chemin+"lapidaire.mategoldmodel",
				"-wapitimodel", chemin+"lapidaire.wapitimodel",
				"-out",	chemin + "res.conll" };
		cnrs.lattice.engines.main.Main.main(arguments);
		req.getRequestDispatcher("/jsp/button-test.jsp").forward(req, resp);
	}

	private void loading(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("ici je lance 1on1");
		showResPage(req, resp);
		
	}
	
	private void showResPage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String nextJSP = "/jsp/result.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        req.setAttribute("result", "true");
        dispatcher.forward(req, resp);
    }   
}