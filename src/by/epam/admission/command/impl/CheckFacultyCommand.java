package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.FacultyService;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CheckFacultyCommand implements ActionCommand {

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) {

        String enrolleeId = request.getParameter("enrolleeId");
        String facultyId = request.getParameter("facultyId");

        int eid = Integer.parseInt(enrolleeId);
        int fid = Integer.parseInt(facultyId);

        try {
            boolean result = FacultyService.checkFaculty(eid, fid);

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

        return null; // STUB
    }
}
