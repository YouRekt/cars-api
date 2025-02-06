package pw.react.cars_api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import pw.react.cars_api.models.Car;
import pw.react.cars_api.models.Customer;
import pw.react.cars_api.models.Rental;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, String> {
    @Query(value = "SELECT case when (count(*) > 0) then true else false end FROM Rental r WHERE r.car = :car AND r.isCancelled = false AND r.startAt<:startAtBefore AND r.endAt<:endAtAfter")
    boolean existsByCarAndStartAtBeforeAndEndAtAfter(Car car, Date startAtBefore, Date endAtAfter);

    boolean existsByCar_Id(String car);
    Optional<Rental> findByCustomerAndId(Customer customer, String id);
    Page<Rental> findByCustomer(Customer customer, Pageable pageable);
    boolean existsByIdAndCustomer(String id, Customer customer);
    List<Rental> findAllByCustomer(Customer customer);

    @Query(value = "SELECT r FROM Rental r WHERE r.customer = :customer AND r.isCancelled = false")
    Page<Rental> findNonCancelledByCustomer(@Param("customer") Customer customer, Pageable pageable);

    @Modifying
    @Query("UPDATE Rental r SET r.isCancelled = true WHERE r.id = :id")
    void cancelById(@Param("id") String id);

    @Modifying
    @Query("UPDATE Rental r SET r.isCancelled = true WHERE r.id = :id AND r.customer = :customer")
    void cancelByIdAndCustomer(@Param("id") String id, @Param("customer") Customer customer);
}
