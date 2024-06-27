package com.blibli.inventory.Repository;

import com.blibli.inventory.Model.*;
import org.springframework.data.jpa.repository.*;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
