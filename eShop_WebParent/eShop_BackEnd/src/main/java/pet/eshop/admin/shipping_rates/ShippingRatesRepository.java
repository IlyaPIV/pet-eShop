package pet.eshop.admin.shipping_rates;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import pet.eshop.common.entity.ShippingRate;


public interface ShippingRatesRepository extends PagingAndSortingRepository<ShippingRate, Integer> {

    @Query("SELECT sr FROM ShippingRate sr WHERE sr.country.name LIKE %?1% OR sr.state LIKE %?1%")
    public Page<ShippingRate> findAll(String keyword, Pageable pageable);

    @Query("SELECT sr FROM ShippingRate sr WHERE sr.country.id = ?1 AND sr.state LIKE ?2")
    public ShippingRate findByCountryAndState(Integer countryID, String state);

    @Query("UPDATE ShippingRate sr SET sr.codSupported = ?2 WHERE sr.id = ?1")
    @Modifying
    public void updateCODSupport(Integer id, boolean enabled);

    public Long countById(Integer id);
}
