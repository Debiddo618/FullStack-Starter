package com.starter.fullstack.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starter.fullstack.api.Inventory;
import com.starter.fullstack.api.UnitOfMeasurement;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class InventoryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  private Inventory inventory;

  @Before
  public void setup() throws Throwable {
    //Create and save the inventory object
    this.inventory = new Inventory();
    this.inventory.setId("test1");
    this.inventory.setName("test1");
    this.inventory.setProductType("test1");
    this.inventory.setDescription("random description");
    this.inventory.setAveragePrice(new BigDecimal(4.13));
    this.inventory.setAmount(new BigDecimal(1));
    this.inventory.setNeverExpires(false);
    this.inventory.setBestBeforeDate(Instant.EPOCH);
    this.inventory.setUnitOfMeasurement(UnitOfMeasurement.CUP);

    this.inventory = this.mongoTemplate.save(this.inventory);
  }

  @After
  public void teardown() {
    this.mongoTemplate.dropCollection(Inventory.class);
  }

    /**
   * Test findAll endpoint.
   * @throws Throwable see MockMvc
   */
  @Test
  public void findAll() throws Throwable {
    this.mockMvc.perform(get("/inventory")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json("[" + this.objectMapper.writeValueAsString(inventory) + "]"));
  }

    /**
   * Test created endpoint.
   * @throws Throwable see MockMvc
   */
  @Test
  public void create() throws Throwable {
    this.inventory = new Inventory();
    this.inventory.setId("test2");
    this.inventory.setName("test2");
    this.inventory.setProductType("test2");
    this.mockMvc.perform(post("/inventory")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(this.inventory)))
        .andExpect(status().isOk());
        
    Assert.assertEquals(2, this.mongoTemplate.findAll(Inventory.class).size());
  }

  /**
   * Test  endpoint.
   * @throws Throwable see MockMvc
   */
  @Test
  public void remove() throws Throwable {
    this.mockMvc.perform(delete("/inventory")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.inventory.getId()))
      .andExpect(status().isOk());
    
    Assert.assertEquals(0, this.mongoTemplate.findAll(Inventory.class).size());
  }

  /**
   * Test  endpoint.
   * @throws Throwable see MockMvc
   */
  @Test
  public void update() throws Throwable {
    //Save the id of the inventory to update
    String ids = this.inventory.getId();

    // create the updated information for inventory
    this.inventory = new Inventory();
    this.inventory.setId("test2");
    this.inventory.setName("Apple");
    this.inventory.setProductType("Fruit");
    this.inventory.setDescription("Red, round, and sweet");
    this.inventory.setAveragePrice(new BigDecimal(2.15));
    this.inventory.setAmount(new BigDecimal(2));
    this.inventory.setNeverExpires(true);
    this.inventory.setBestBeforeDate(Instant.parse("1980-04-09T10:15:30.00Z"));
    this.inventory.setUnitOfMeasurement(UnitOfMeasurement.POUND);

    this.mockMvc.perform(put("/inventory/" + ids)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(this.inventory)))
      .andExpect(status().isOk());
    
    Inventory updatedInventory = this.mongoTemplate.findById(ids, Inventory.class);


    Assert.assertEquals("test1", updatedInventory.getId());
    Assert.assertEquals("Apple", updatedInventory.getName());
    Assert.assertEquals("Fruit", updatedInventory.getProductType());
    Assert.assertEquals("Red, round, and sweet", updatedInventory.getDescription());
    Assert.assertEquals(new BigDecimal(2.15), updatedInventory.getAveragePrice());
    Assert.assertEquals(new BigDecimal(2), updatedInventory.getAmount());
    Assert.assertTrue(updatedInventory.isNeverExpires());
    Assert.assertEquals(Instant.parse("1980-04-09T10:15:30.00Z"), updatedInventory.getBestBeforeDate());
    Assert.assertEquals(UnitOfMeasurement.POUND, updatedInventory.getUnitOfMeasurement());
  }

}
