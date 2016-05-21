/*
 *  Waltz
 * Copyright (c) David Watkins. All rights reserved.
 * The use and distribution terms for this software are covered by the
 * Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 * which can be found in the file epl-v10.html at the root of this distribution.
 * By using this software in any fashion, you are agreeing to be bound by
 * the terms of this license.
 * You must not remove this notice, or any other, from this software.
 *
 */

import _ from "lodash";
import BaseLookupService from "./BaseLookupService";
import {
    applicationKindDisplayNames,
    assetCostKindNames,
    authSourceRatingNames,
    bookmarkNames,
    capabilityRatingNames,
    changeInitiativeNames,
    entityNames,
    investmentRatingNames,
    involvementKindNames,
    lifecyclePhaseDisplayNames,
    orgUnitKindNames,
    severityNames
} from "./display_names";
import {bookmarkIconNames, entityIconNames, severityIconNames, ragIconNames} from "./icon_names";


const displayNameService = new BaseLookupService();
const iconNameService = new BaseLookupService();
const descriptionService = new BaseLookupService();

export default (module) => {
    module.service('WaltzDisplayNameService', () => displayNameService);
    module.service('WaltzIconNameService', () => iconNameService);
    module.service('WaltzDescriptionService', () => descriptionService);

    displayNameService.register('applicationKind', applicationKindDisplayNames);
    displayNameService.register('assetCost', assetCostKindNames);
    displayNameService.register('bookmark', bookmarkNames);
    displayNameService.register('capabilityRating', capabilityRatingNames);
    displayNameService.register('changeInitiative', changeInitiativeNames);
    displayNameService.register('entity', entityNames);
    displayNameService.register('investmentRating', investmentRatingNames);
    displayNameService.register('involvementKind', involvementKindNames);
    displayNameService.register('lifecyclePhase', lifecyclePhaseDisplayNames);
    displayNameService.register('orgUnitKind', orgUnitKindNames);
    displayNameService.register('rating', authSourceRatingNames);
    displayNameService.register('severity', severityNames);

    iconNameService.register('bookmark', bookmarkIconNames);
    iconNameService.register('entity', entityIconNames);
    iconNameService.register('severity', severityIconNames);
    iconNameService.register('rag', ragIconNames);

    module.run([
        'DataTypesDataService',
        (DataTypesDataService) =>
            DataTypesDataService
                .findAll()
                .then(results => {
                    const indexed = _.keyBy(results, 'code');
                    displayNameService.register('dataType', _.mapValues(indexed, 'name'));
                    descriptionService.register('dataType', _.mapValues(indexed, 'description'));
                })
    ]);

};
