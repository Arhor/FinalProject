package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.FacultyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterToFacultyCommand implements ActionCommand {

    private static final Logger LOG = LogManager.getLogger(RegisterToFacultyCommand.class);

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) {

        boolean result;

        String enrolleeId = request.getParameter("enrolleeId");
        String facultyId = request.getParameter("facultyId").replaceAll("[^0-9]","");

        int eid = Integer.parseInt(enrolleeId);
        int fid = Integer.parseInt(facultyId);

        LOG.debug("Enrolle ID: " + eid + "\nFaculty ID: " + fid);

        try {
            if (FacultyService.checkInactive(eid, fid)) {
                result = FacultyService.restoreFacultyRegistration(eid, fid);
            } else {
                result = FacultyService.registerToFaculty(eid, fid);
            }

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("faculty", fid);
                jsonObject.put("result", result);
                response.setContentType("application/json");
                response.getWriter().write(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (ProjectException e) {
            e.printStackTrace(); // TODO: STUB
        } catch (IOException e) {
            e.printStackTrace(); // TODO: STUB
        }




        return null;
    }
}
