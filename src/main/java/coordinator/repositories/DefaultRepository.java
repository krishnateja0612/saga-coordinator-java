package coordinator.repositories;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.MongoCursor;
import com.google.gson.Gson;
import static com.mongodb.client.model.Filters.*;

public class DefaultRepository<TKey, TEntity> implements Repository<TKey, TEntity> {

    private final MongoCollection<Document> mongoCollection;
    private final String keyName;
    private final Gson gson;
    private final Class<TEntity> beanTypeTEntity;

    public DefaultRepository(String connectionString, String database, String collection, String keyName) {
        this.mongoCollection = getMongoCollection(connectionString, database, collection);
        this.keyName = keyName;
        this.gson = new Gson();
        this.beanTypeTEntity = getBeanType();
    }

    private MongoCollection<Document> getMongoCollection(String connectionString, String database, String collection) {
        MongoClientURI uri = new MongoClientURI(connectionString);
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);

        mongoClient.close();

        return mongoDatabase.getCollection(collection);
    }

    @Override
    public TEntity add(TEntity value) {
        Document document = convertFromEntityToDocument(value);
        mongoCollection.insertOne(document);

        return value;
    }

    @Override
    public TEntity update(TKey key, TEntity value) {
        Document document = convertFromEntityToDocument(value);
        mongoCollection.updateOne(eq(keyName, key), new Document("$set", document));

        return value;
    }

    @Override
    public void delete(TKey key) {
        mongoCollection.deleteOne(eq(keyName, key));
    }

    @Override
    public TEntity findByKey(TKey key) {
        Document myDocObj = mongoCollection.find(eq(keyName, key)).first();
        TEntity value = convertFromDocumentToEntity(myDocObj);

        return value;
    }

    private TEntity findByFilter(Bson filter) {
        Document myDocObj = mongoCollection.find(filter).first();
        TEntity value = convertFromDocumentToEntity(myDocObj);

        return value;
    }

    @Override
    public List<TEntity> findManyByFilter(Object filter) {
        List<TEntity> list = new ArrayList<TEntity>();
        MongoCursor<Document> cursor = mongoCollection.find((Bson) filter).iterator();

        try {
            while (cursor.hasNext()) {
                convertFromDocumentToEntity(cursor.next());
            }
        } finally {
            cursor.close();
        }

        return list;
    }

    private Document convertFromEntityToDocument(TEntity value) {
        String json = gson.toJson(value);
        Document document = Document.parse(json);

        return document;
    }

    private TEntity convertFromDocumentToEntity(Document document) {
        String myDocJson = document.toJson();
        TEntity value = this.gson.fromJson(myDocJson, this.beanTypeTEntity);

        return value;
    }

    @SuppressWarnings("unchecked")
    private Class<TEntity> getBeanType() {
        return ((Class<TEntity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
    }

    @Override
    public TEntity findByFilter(String key1, String value1) {
        return findByFilter(eq(key1, value1));
    }

    @Override
    public TEntity findByFilter(String key1, String value1, String key2, String value2) {
        return findByFilter(and(eq(key1, value1), eq(key2, value2)));
    }
}
