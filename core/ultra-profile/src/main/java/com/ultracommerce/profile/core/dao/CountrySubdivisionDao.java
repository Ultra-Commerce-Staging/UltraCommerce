/*
 * #%L
 * UltraCommerce Profile
 * %%
 * Copyright (C) 2009 - 2016 Ultra Commerce
 * %%
 * Licensed under the Ultra Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.ultracommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Ultra in which case
 * the Ultra End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.ultracommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Ultra Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */
package com.ultracommerce.profile.core.dao;

import com.ultracommerce.profile.core.domain.CountrySubdivision;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public interface CountrySubdivisionDao {

    public List<CountrySubdivision> findSubdivisions();

    public List<CountrySubdivision> findSubdivisions(String countryAbbreviation);

    public List<CountrySubdivision> findSubdivisionsByCountryAndCategory(String countryAbbreviation, String category);

    public CountrySubdivision findSubdivisionByAbbreviation(String abbreviation);

    public CountrySubdivision findSubdivisionByCountryAndAltAbbreviation(@Nonnull String countryAbbreviation, @Nonnull String altAbbreviation);

    public CountrySubdivision findSubdivisionByCountryAndName(@Nonnull String countryAbbreviation, @Nonnull String name);

    public CountrySubdivision create();

    public CountrySubdivision save(CountrySubdivision subdivision);

}
