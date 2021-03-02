package eu.geekhome.rest;

import eu.geekhome.automation.blocks.IBlockFactoriesCollector;
import org.jvnet.hk2.annotations.Service;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

@Service
public class BlockFactoriesCollectorHolderService extends HolderService<IBlockFactoriesCollector> {

    public BlockFactoriesCollectorHolderService(@Context Application app) throws Exception {
        super(app, IBlockFactoriesCollector.class);
    }
}