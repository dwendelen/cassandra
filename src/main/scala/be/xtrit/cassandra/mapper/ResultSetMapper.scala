package be.xtrit.cassandra.mapper

import java.util
import be.xtrit.cassandra.user.model.Credentials
import com.datastax.driver.core.{ResultSet, Row}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class ResultSetMapper {

    @Autowired
    private var classMapperFactory: ClassMappingFactory = null

    private var map = Map[Class[_], ClassMapping[_]]()
    private def createOrGetMap[T](clazz: Class[T]): ClassMapping[T] = {
        if(!map.contains(clazz)) {
            map += (clazz -> classMapperFactory.create(clazz))
        }

        map(clazz).asInstanceOf[ClassMapping[T]]
    }

    def mapResultSet[T](resultSet: ResultSet, clazz: Class[T]): List[T] = {
        val mapping = createOrGetMap(clazz)
        val iterator: util.Iterator[Row] = resultSet.iterator()

        mapRows(iterator, mapping, clazz, Nil)
    }

    def mapSingleResult[T](resultSet: ResultSet, clazz: Class[T]): T = {
        val all: util.List[Row] = resultSet.all
        if(all.size() != 1) {
            throw new IllegalStateException("There must be exactly 1 row")
        }

        val mapping = createOrGetMap(clazz)
        mapping.mapRow(all.get(0))
    }

    private def mapRows[T](iterator: util.Iterator[Row], classMapping: ClassMapping[T], clazz: Class[T], mappedUntilNow: List[T]) :List[T] =
        if(!iterator.hasNext) {
            mappedUntilNow
        } else {
            val mappedRow = classMapping.mapRow(iterator.next())
            mapRows(iterator, classMapping, clazz, mappedRow::mappedUntilNow)
        }
}
