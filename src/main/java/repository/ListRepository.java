package repository;

import entities.ItemList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRepository extends JpaRepository<ItemList, Long> {
}
