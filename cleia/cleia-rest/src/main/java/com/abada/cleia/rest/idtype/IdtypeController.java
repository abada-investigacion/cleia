/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.idtype;

import com.abada.cleia.dao.IdTypeDao;
import com.abada.cleia.entity.user.IdType;
import com.abada.extjs.ExtjsStore;
import com.abada.extjs.Success;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.factory.GridRequestFactory;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author mmartin
 */
@Controller
@RequestMapping("/rs/idtype")
public class IdtypeController {

    private static final Log logger = LogFactory.getLog(IdtypeController.class);
    @Autowired
    private IdTypeDao idtypeDao;

    /**
     * Returns all IdType.
     * @return Return all IdType.
     */
    @RolesAllowed(value={"ROLE_ADMIN","ROLE_USER","ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ExtjsStore getAllIdType() {

        ExtjsStore aux = new ExtjsStore();
        List<IdType> lidtype = idtypeDao.getAllIdType();
        aux.setTotal(lidtype.size());
        aux.setData(lidtype);
        return aux;
    }

    /**
     * Returns one IdType by id
     * @param ididtype IdType id.
     * @return Return one IdType.
     */    
    @RolesAllowed(value={"ROLE_ADMIN","ROLE_USER","ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{ididtype}", method = RequestMethod.GET)
    public IdType getIdTypeById(@PathVariable Integer ididtype) {

        IdType idtype = new IdType();
        try {
            idtype = idtypeDao.getIdTypeById(ididtype);
        } catch (Exception e) {
            logger.error(e);
        }

        return idtype;
    }

    /**
     * Search a list of IdType by params
     * @param filter Filter conditions of results. Set in JSON by an array of FilterRequestPriv
     * @param sort Order of results. Set in JSON by an array of OrderByRequest
     * @param limit Set the limit of the results. Use it for pagination
     * @param start Set the start of the results. Use it for pagination
     * @return Return a list of results.
     */
    @RolesAllowed(value={"ROLE_ADMIN","ROLE_USER","ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ExtjsStore getSearchIdType(String filter, String sort, Integer limit, Integer start) {

        List<IdType> lidtype = new ArrayList<IdType>();
        ExtjsStore aux = new ExtjsStore();
        try {
            GridRequest grequest = GridRequestFactory.parse(sort, start, limit, filter);
            lidtype = this.idtypeDao.getAll(grequest);
            aux.setData(lidtype);
            aux.setTotal(this.idtypeDao.loadSizeAll(grequest).intValue());
        } catch (Exception e) {
            logger.error(e);
        }

        return aux;
    }

    /**
     * Insert a IdType
     * @param idtype IdType structure. Must set in JSON in the Http body request.
     * @return Return success structure.
     */
    @RolesAllowed(value={"ROLE_ADMIN","ROLE_USER","ROLE_ADMINISTRATIVE"})
    @RequestMapping(method = RequestMethod.POST)
    public Success postIdType(@RequestBody IdType idtype) {

        Success result = new Success(Boolean.FALSE);
        try {
            idtypeDao.postIdType(idtype);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }

        return result;
    }

    /**
     * Modify a IdType by id
     * @param ididtype IdType id.
     * @param idtype IdType structure. Must set in JSON in the Http body request.
     * @return Return success structure.
     */
    @RolesAllowed(value={"ROLE_ADMIN","ROLE_USER","ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{ididtype}", method = RequestMethod.PUT)
    public Success putIdType(@PathVariable Integer ididtype, @RequestBody IdType idtype) {

        Success result = new Success(Boolean.FALSE);
        try {
            idtypeDao.putIdType(ididtype, idtype);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }

        return result;
    }

    /**
     * Delete a IdType by id
     * @param ididtype IdType id.
     * @return Return success structure.
     */
    @RolesAllowed(value={"ROLE_ADMIN","ROLE_USER","ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{ididtype}", method = RequestMethod.DELETE)
    public Success deleteIdType(@PathVariable Integer ididtype) {

        Success result = new Success(Boolean.FALSE);
        try {
            idtypeDao.deleteIdType(ididtype);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }

        return result;
    }
}
