package com.khartec.waltz.web.endpoints.api;

import com.khartec.waltz.model.command.CommandResponse;
import com.khartec.waltz.model.invovement_kind.InvolvementKind;
import com.khartec.waltz.model.invovement_kind.InvolvementKindChangeCommand;
import com.khartec.waltz.model.invovement_kind.InvolvementKindCreateCommand;
import com.khartec.waltz.model.user.Role;
import com.khartec.waltz.service.involvement_kind.InvolvementKindService;
import com.khartec.waltz.service.user.UserRoleService;
import com.khartec.waltz.web.WebUtilities;
import com.khartec.waltz.web.endpoints.Endpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spark.Request;
import spark.Response;

import java.io.IOException;

import static com.khartec.waltz.common.Checks.checkNotNull;
import static com.khartec.waltz.web.WebUtilities.*;
import static com.khartec.waltz.web.endpoints.EndpointUtilities.*;

@Service
public class InvolvementKindEndpoint implements Endpoint {

    private static final Logger LOG = LoggerFactory.getLogger(InvolvementKindEndpoint.class);
    private static final String BASE_URL = WebUtilities.mkPath("api", "involvement-kind");

    private final InvolvementKindService service;
    private UserRoleService userRoleService;


    @Autowired
    public InvolvementKindEndpoint(InvolvementKindService service, UserRoleService userRoleService) {
        checkNotNull(service, "service must not be null");
        checkNotNull(userRoleService, "userRoleService cannot be null");

        this.service = service;
        this.userRoleService = userRoleService;
    }


    @Override
    public void register() {

        // read
        getForList(BASE_URL, (request, response) -> service.findAll());
        getForDatum(mkPath(BASE_URL, "id", ":id"), this::getByIdRoute );

        // create
        postForDatum(mkPath(BASE_URL, "update"), this::createInvolvementKindRoute );

        // update
        putForDatum(mkPath(BASE_URL, "update"), this::updateInvolvementKindRoute );

        // delete
        deleteForDatum(mkPath(BASE_URL, ":id"), this::deleteInvolvementKindRoute);

    }


    private InvolvementKind getByIdRoute(Request request, Response response) {
        long id = getId(request);
        return service.getById(id);
    }


    private Long createInvolvementKindRoute(Request request, Response response) throws IOException {
        ensureUserHasAdminRights(request);

        InvolvementKindCreateCommand command = readBody(request, InvolvementKindCreateCommand.class);
        String username = getUsername(request);
        LOG.info("User: {} creating Involvement Kind: {}", username, command);

        return service.create(command, username);
    }


    private CommandResponse<InvolvementKindChangeCommand> updateInvolvementKindRoute(Request request, Response response)
            throws IOException {
        ensureUserHasAdminRights(request);

        String username = getUsername(request);
        InvolvementKindChangeCommand command = readBody(request, InvolvementKindChangeCommand.class);

        LOG.info("User: {} updating Involvement Kind: {}", username, command);
        return service.update(command, username);
    }


    private boolean deleteInvolvementKindRoute(Request request, Response response) {
        ensureUserHasAdminRights(request);

        long id = getId(request);
        String username = getUsername(request);

        LOG.info("User: {} removing Involvement Kind: {}", username, id);

        return service.delete(id);
    }


    private void ensureUserHasAdminRights(Request request) {
        requireRole(userRoleService, request, Role.ADMIN);
    }

}
