package com.starter.fullstack.dao;

import com.starter.fullstack.api.Inventory;
import com.starter.fullstack.api.UnitOfMeasurement;
import com.starter.fullstack.config.EmbedMongoClientOverrideConfig;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test Inventory DAO.
 */
@ContextConfiguration(classes = {EmbedMongoClientOverrideConfig.class})
@DataMongoTest
@RunWith(SpringRunner.class)
public class InventoryDAOTest {
  @Resource
  private MongoTemplate mongoTemplate;
  private InventoryDAO inventoryDAO;
  private static final String ID = "tempId";
  private static final String NAME = "Amber";
  private static final String PRODUCT_TYPE = "hops";
  private static final String DESCRIPTION = "random description";
  private static final BigDecimal AVERAGE_PRICE = new BigDecimal(4.13);
  private static final BigDecimal AMOUNT = new BigDecimal(1);
  private static final Boolean NEVER_EXPIRES = false;
  private static final Instant BEST_BEFORE_DATE = Instant.EPOCH;
  private static final UnitOfMeasurement UNIT = UnitOfMeasurement.CUP;




  @Before
  public void setup() {
    this.inventoryDAO = new InventoryDAO(this.mongoTemplate);
    Inventory First = new Inventory();
    First.setId("test1");
    this.mongoTemplate.save(First);
  }

  @After
  public void tearDown() {
    this.mongoTemplate.dropCollection(Inventory.class);
  }

  /**
   * Test Find All method.
   */
  @Test
  public void findAll() {
    Inventory inventory = new Inventory();
    inventory.setId(ID);
    inventory.setName(NAME);
    inventory.setProductType(PRODUCT_TYPE);
    this.mongoTemplate.save(inventory);
    List<Inventory> actualInventory = this.inventoryDAO.findAll();
    Assert.assertFalse(actualInventory.isEmpty());
  }

  /**
   * Testing the create Method
   */
  @Test
  public void create() {
    Inventory inventory = new Inventory();
    inventory.setId(ID);
    inventory.setName(NAME);
    inventory.setProductType(PRODUCT_TYPE);
    this.inventoryDAO.create(inventory);

    List<Inventory> actualInventory = this.inventoryDAO.findAll();
    Inventory createdInventory = actualInventory.get(0);

    Assert.assertTrue(createdInventory.getId() != null);
    Assert.assertFalse(actualInventory.isEmpty());
  }

  /**
   * Testing the delete Method
   */
  @Test
  public void deleted() {
    Inventory inventory = new Inventory();
    inventory.setId(ID);
    inventory.setName(NAME);
    inventory.setProductType(PRODUCT_TYPE);
    this.mongoTemplate.save(inventory);

    Optional<Inventory> temp = this.inventoryDAO.delete(inventory.getId());

    Assert.assertEquals(temp.get().getId(), ID);
    Assert.assertTrue(this.inventoryDAO.findAll().size() == 1);
  }

  /**
   * Testing the update Method
   */
  @Test
  public void update() {
    Inventory inventory = new Inventory();
    inventory.setId(ID);
    inventory.setName(NAME);
    inventory.setProductType(PRODUCT_TYPE);
    inventory.setAveragePrice(AVERAGE_PRICE);
    inventory.setDescription(DESCRIPTION);
    inventory.setAmount(AMOUNT);
    inventory.setNeverExpires(NEVER_EXPIRES);
    inventory.setBestBeforeDate(BEST_BEFORE_DATE);
    inventory.setUnitOfMeasurement(UNIT);

    this.mongoTemplate.save(inventory);

    Inventory updateInventory = new Inventory();
    updateInventory.setName("Tomato");
    updateInventory.setProductType("Veggies");
    updateInventory.setAveragePrice(new BigDecimal(5));
    updateInventory.setDescription("might also be a fruit");
    updateInventory.setNeverExpires(true);
    updateInventory.setAmount(new BigDecimal(2));
    updateInventory.setBestBeforeDate(Instant.parse("1980-04-09T10:15:30.00Z"));
    updateInventory.setUnitOfMeasurement(UnitOfMeasurement.POUND);

    System.out.println(inventory.getId());

    Optional<Inventory> temp = this.inventoryDAO.update(inventory.getId(), updateInventory);
    
    Assert.assertTrue(temp.get().getName().equals("Tomato"));
    Assert.assertTrue(temp.get().getProductType().equals("Veggies"));
    Assert.assertTrue(temp.get().getDescription().equals("might also be a fruit"));
    Assert.assertTrue(temp.get().getAveragePrice().compareTo(new BigDecimal(5)) == 0);
    Assert.assertTrue(temp.get().getAmount().compareTo(new BigDecimal(2)) == 0);
    Assert.assertTrue(temp.get().isNeverExpires() == true);
    Assert.assertTrue(temp.get().getBestBeforeDate().compareTo(Instant.parse("1980-04-09T10:15:30.00Z")) == 0);
    Assert.assertTrue(temp.get().getUnitOfMeasurement() == UnitOfMeasurement.POUND);
  }

}
