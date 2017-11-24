/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Based on the helpful answer at http://stackoverflow.com/q/25356781/56285,
 * with error details in response body added.
 * 
 * @author Joni Karppinen
 * @since 20.2.2015
 */
@RestController
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error.html";

    @Autowired
    private ErrorAttributes errorAttributes;

@RequestMapping(value = PATH)
ResponseEntity error(HttpServletRequest request, HttpServletResponse response) {
HttpHeaders responseHeaders = new HttpHeaders();
HttpStatus httpStatus = org.springframework.http.HttpStatus.valueOf(response.getStatus());

/*
<style>
.nb-error {
  margin: 0 auto;
  text-align: center;
  max-width: 480px;
  padding: 60px 30px;
}

.nb-error .error-code {
  color: #2d353c;
  font-size: 46px;
  line-height: 100px;
}

.nb-error .error-desc {
  font-size: 12px;
  color: #647788;
}
.nb-error .input-group{
  margin: 30px 0;
}
</style>


<div class="nb-error">
      <div class="error-code">Access Denied</div>
      <h3 class="font-bold">You do not have permission to use Notification Backbone Admin Interface</h3>

      <div class="error-desc">
If you have any queries about using the Notifications Service then you can contact the <a href="mailto:is.helpline@ed.ac.uk">IS Helpline</a> and your call will be directed to the most appropriate person to deal with your request.
          <!--
          <div class="input-group">
            <input type="text' placeholder="Try with a search' class="form-control">
            <span class="input-group-btn">
               <button type="button' class="btn btn-default">
                  <em class="fa fa-search"></em>
               </button>
            </span>
         </div>
         
         <ul class="list-inline text-center text-sm">
           <li class="list-inline-item"><a href="http://nextbootstrap.com' class="text-muted">Go to App</a>
           </li>
           <li class="list-inline-item"><a href="http://nextbootstrap.com' class="text-muted">Login</a>
           </li>
           <li class="list-inline-item"><a href="http://nextbootstrap.com' class="text-muted">Register</a>
           </li>
         </ul>
         -->
         <br/><br/>
        <div class="text-center">
          <span>©</span>
          <span></span>
          <span></span>
          <span>The University of Edinburgh</span>
          <br>
          <!--
          <span>Free bootstrap code example</span>
          -->
       </div>
      </div>
  </div>
   
 */

String html = "<style>.nb-error{margin: 0 auto; text-align: center; max-width: 480px; padding: 60px 30px;}.nb-error .error-code{color: #2d353c; font-size: 46px; line-height: 100px;}.nb-error .error-desc{font-size: 12px; color: #647788;}.nb-error .input-group{margin: 30px 0;}</style><div class=\"nb-error\"> <div class=\"error-code\">Access Denied</div><h3 class=\"font-bold\">You do not have permission to use Notification Backbone Admin Interface</h3> <div class=\"error-desc\">If you have any queries about using the Notifications Service then you can contact the <a href=\"mailto:is.helpline@ed.ac.uk\">IS Helpline</a> and your call will be directed to the most appropriate person to deal with your request.<!-- <div class=\"input-group\"> <input type=\"text' placeholder=\"Try with a search' class=\"form-control\"> <span class=\"input-group-btn\"> <button type=\"button' class=\"btn btn-default\"> <em class=\"fa fa-search\"></em> </button> </span> </div><ul class=\"list-inline text-center text-sm\"> <li class=\"list-inline-item\"><a href=\"http://nextbootstrap.com' class=\"text-muted\">Go to App</a> </li><li class=\"list-inline-item\"><a href=\"http://nextbootstrap.com' class=\"text-muted\">Login</a> </li><li class=\"list-inline-item\"><a href=\"http://nextbootstrap.com' class=\"text-muted\">Register</a> </li></ul> --> <br/><br/> <div class=\"text-center\"> <span>©</span> <span></span> <span></span> <span>The University of Edinburgh</span> <br><!-- <span>Free bootstrap code example</span> --> </div></div></div>";

return new ResponseEntity(html, responseHeaders,httpStatus);
}    
    
    
    @Override
    public String getErrorPath() {
        return PATH;
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }

}