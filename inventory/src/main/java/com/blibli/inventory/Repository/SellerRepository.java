package com.blibli.inventory.Repository;

import com.blibli.inventory.Model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Long> {
}
