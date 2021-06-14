package eu.geekhome.rest;

import eu.geekhome.domain.repository.Repository;
import org.jvnet.hk2.annotations.Service;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

@Service
public class RepositoryHolderService extends HolderService<Repository> {

    public RepositoryHolderService(@Context Application app) throws Exception {
        super(app, Repository.class);
    }
}