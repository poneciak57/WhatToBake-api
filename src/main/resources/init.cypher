// Query to initialize database locally for tests
CREATE

// Categories
(c1:CATEGORY{name:"category1", create_date: datetime()}),
(c2:CATEGORY{name:"category2", create_date: datetime()}),
(c3:CATEGORY{name:"category3", create_date: datetime()}),

// Products//
(p1:PRODUCT{name:"product1", create_date: datetime()})-[:HAS_CATEGORY]->(c1),
(p2:PRODUCT{name:"product2", create_date: datetime()})-[:HAS_CATEGORY]->(c1),
(p3:PRODUCT{name:"product3", create_date: datetime()})-[:HAS_CATEGORY]->(c2),
(p4:PRODUCT{name:"product4", create_date: datetime()})-[:HAS_CATEGORY]->(c2),
(p5:PRODUCT{name:"product5", create_date: datetime()})-[:HAS_CATEGORY]->(c1),
(p6:PRODUCT{name:"product6", create_date: datetime()}),

// Recipes
(r1:RECIPE{title:"recipe1",link:"-",image:"-", create_date: datetime()}),
(r2:RECIPE{title:"recipe2",link:"-",image:"-", create_date: datetime()}),
(r3:RECIPE{title:"recipe3",link:"-",image:"-", create_date: datetime()}),
(r4:RECIPE{title:"recipe4",link:"-",image:"-", create_date: datetime()}),
(r5:RECIPE{title:"recipe5",link:"-",image:"-", create_date: datetime()}),
(r6:RECIPE{title:"recipe6",link:"-",image:"-", create_date: datetime()}),
(r7:RECIPE{title:"recipe7",link:"-",image:"-", create_date: datetime()}),
(r8:RECIPE{title:"recipe8",link:"-",image:"-", create_date: datetime()}),

// Tags
(t1:TAG{name:"tag1", create_date: datetime()}),
(t2:TAG{name:"tag2", create_date: datetime()}),
(t3:TAG{name:"tag3", create_date: datetime()}),
(t4:TAG{name:"tag4", create_date: datetime()}),

// Recipe tags relations
(r1)-[:HAS_TAG]->(t1),
(r2)-[:HAS_TAG]->(t2),
(r3)-[:HAS_TAG]->(t1),
(r4)-[:HAS_TAG]->(t3),
(r5)-[:HAS_TAG]->(t3),
(r6)-[:HAS_TAG]->(t1),
(r7)-[:HAS_TAG]->(t2),

// Recipe Products relations
(r1)-[:NEEDS]->(p1),
(r1)-[:NEEDS]->(p3),
(r1)-[:NEEDS]->(p4),
(r2)-[:NEEDS]->(p5),
(r2)-[:NEEDS]->(p1),
(r3)-[:NEEDS]->(p2),
(r5)-[:NEEDS]->(p2),
(r5)-[:NEEDS]->(p3),
(r5)-[:NEEDS]->(p4),
(r5)-[:NEEDS]->(p5),
(r6)-[:NEEDS]->(p2),
(r6)-[:NEEDS]->(p3),
(r7)-[:NEEDS]->(p2),
(r7)-[:NEEDS]->(p5),
(r7)-[:NEEDS]->(p1)



