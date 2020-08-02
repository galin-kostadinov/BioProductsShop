package org.gkk.bioshopapp.init;

import org.gkk.bioshopapp.config.InitRootUser;
import org.gkk.bioshopapp.data.model.ProductType;
import org.gkk.bioshopapp.service.model.product.ProductCreateServiceModel;
import org.gkk.bioshopapp.service.service.AuthService;
import org.gkk.bioshopapp.service.service.CategoryService;
import org.gkk.bioshopapp.service.service.ProductService;
import org.gkk.bioshopapp.service.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInit implements CommandLineRunner {
    private final CategoryService categoryService;
    private final RoleService roleService;
    private final ProductService productService;
    private final AuthService authService;
    private final InitRootUser initRootUser;


    private static final List<ProductCreateServiceModel> PRODUCTS = Arrays.asList(
            new ProductCreateServiceModel("Banana", "A123CD", "Fyffes", ProductType.FRUIT, "You probably know that bananas are grown in sunny, tropical areas and most of ours come from Central and South America where they have been growing fabulous bananas for decades. The banana story – from growing the best bananas in a sustainable way, then harvesting, shipping and ripening them to meet the specific needs of the people who eat them and the retailers who sell them – is pretty amazing. So, strap yourself in and here goes...", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSS4JW3ckveu5v2MNd7HIEKhPDdD99dpS5DWA&usqp=CAU", BigDecimal.valueOf(2.2)),
            new ProductCreateServiceModel("Pineapple", "A123CE", "Fyffes", ProductType.FRUIT, "The pineapple, indigenous to the Americas, was known by the Guarani Indians as ‘nana, the excellent fruit’. It wasn’t until 1493 that Christopher Columbus “discovered” pineapples on the island of Guadeloupe and named them \"Pina de Indes\" – or pine of the Indians, which eventually became the name we know today.", "https://pictures.attention-ngn.com//portal/30/45510/products/1422391036.6593_115_o.jpg", BigDecimal.valueOf(3.5)),
            new ProductCreateServiceModel("Melon", "A123CF", "Fyffes", ProductType.FRUIT, "Have you tried a Fyffes melon?  It’s hard to beat the deliciously refreshing natural taste of a ripe melon and with a host of different varieties available there’s sure to be a melon to delight everyone!", "https://www.fyffes.com/uploads/images/pages/Melons_2nd.jpg", BigDecimal.valueOf(3.2)),
            new ProductCreateServiceModel("Mushrooms", "A123CG", "Fyffes", ProductType.VEGETABLE, "You may be surprised to see mushrooms from a company so well known for its tropical fruit, but in our quest to broaden our horizons and bring our customers healthy, naturally nutritious food, it’s actually a natural step. Fat free, low in cholesterol and packed with vitamins and minerals, mushrooms are a delicious addition to the Fyffes family.", "https://www.perishablenews.com/wp-content/uploads/2018/12/I-0055100.jpg", BigDecimal.valueOf(2.2)),
            new ProductCreateServiceModel("Apples", "L442CG", "DioBio", ProductType.FRUIT, "The fruit is with yellow colored and rose-red blended color.\nTastes sweet-sour, with specific aroma.", "https://images.heb.com/is/image/HEBGrocery/000320625", BigDecimal.valueOf(1.2)),
            new ProductCreateServiceModel("Cucumbers", "L442CN", "Almanac", ProductType.VEGETABLE, "An easy-care vegetable that loves sun and water, cucumbers grow quickly as long as they receive consistent watering and warmth. Most varieties will grow in any amount of space, thanks to the plant’s ability to climb. Of course, these prolific veggies are perfect for pickling!", "https://www.almanac.com/sites/default/files/image_nodes/cucumbers.jpg", BigDecimal.valueOf(1.2)),
            new ProductCreateServiceModel("Tomato", "L442CO", "Bradley", ProductType.VEGETABLE, "A pink, disease resistant variety released in 1961, Bradley Tomato produces a compact, bushy, determinate plant that yields generous amounts of deliciously sweet, old-fashioned flavored tomatoes. Fruit ripens at the same time, making it a great variety for canning and freezing.", "https://www.edenbrothers.com/store/media/Seeds-Vegetables/Tomato-Bradley.jpg", BigDecimal.valueOf(2.2)),
            new ProductCreateServiceModel("Eggplant", "L442CP", "Annina", ProductType.VEGETABLE, "Performs just as you expect a hybrid to, but with the aesthetic and flavor of the most cherished heirloom. We have loved this specialty eggplant from the first time we saw it, before it had a name, and are delighted to finally offer it in our catalog. Beautiful Italian-type fruit offer gratifying harvests from vigorous plants with no spines. From Vitalis Organic Seeds.", "https://www.highmowingseeds.com/media/catalog/product/cache/47e325b677851f562e223168f21f4516/2/4/2477.jpg", BigDecimal.valueOf(2.2)),
            new ProductCreateServiceModel("Red Bell Pepper", "L442CT", "BJ's Wholesale Club", ProductType.VEGETABLE, "A single organic bell pepper contains double the recommended daily allowance of Vitamin C.\nGood source of folic acid\nVitamin A, antioxidants, fiber and iron.", "https://s7d6.scene7.com/is/image/bjs/29115?$bjs-Zoom$", BigDecimal.valueOf(4.99)),
            new ProductCreateServiceModel("Green Pepper", "L442CM", "Farm Park", ProductType.VEGETABLE, "Green and firm.Farm fresh (Directly from the farm) and readily available for delivery. We give you the opportunity to eat healthy and stay healthy.", "https://www.farmpark.ng/wp-content/uploads/2019/04/Peppers-Green-579bb7fc5f9b589aa9661c03.jpg", BigDecimal.valueOf(2.99)),
            new ProductCreateServiceModel("Hot Pepper", "L443CM", "Farm Park", ProductType.VEGETABLE, "In 2003 we had our first experience working on a farm in Willow Creek, California. That year Andrew saved his first seeds, too: an heirloom tomato, and a Thai pepper. Now, 17 years later, the legacy of that first saved seed lives on in the Adaptive Early Thai pepper. We’ve been growing out this seed ever since, sometimes crossing in new Thai types, slowly creating an early maturing Thai pepper. There is some variation in this population, but it centers very strongly on a Thai pepper theme. Some plants hold fruit upwards, toward the sky, and others hang fruit down.", "https://www.adaptiveseeds.com/wp-content/uploads/2014/12/pepper-adaptive-early-thai-2.jpg", BigDecimal.valueOf(1.99)),
            new ProductCreateServiceModel("Oranges", "N443CM", "Farm Park", ProductType.FRUIT, "Oranges are a type of low calorie, highly nutritious citrus fruit. As part of a healthful and varied diet, oranges contribute to strong, clear skin and can help lower a person’s risk of many conditions.", "https://sc02.alicdn.com/kf/HTB1_HOcJFXXXXc6XXXXq6xXFXXX9.jpg_350x350.jpg", BigDecimal.valueOf(2.99)),
            new ProductCreateServiceModel("Cherries", "N444CM", "Envato", ProductType.FRUIT, "We grow five different cherry varieties at Frog Hollow Farm. Our harvest begins with our new Royal Tioga variety that's followed by Farmer Al's favorite Brooks. Sweet, white-fleshed Rainier cherries are next to ripen, and soon after comes everyone's favorite - the Bing. Our long-stemmed Stella cherries are the last to ripen and end our brief but very yummy cherry season on a sweet note.", "https://www.freshfruitportal.com/assets/uploads/2017/08/cerezas_53336629-1024x683.jpg", BigDecimal.valueOf(5.99)),
            new ProductCreateServiceModel("Strawberry", "N424CM", "Envato", ProductType.FRUIT, "As the strawberry needle sabotage scare widens across Australia, supermarkets are pulling punnets from their shelves while farmers are dumping their harvests in fields.\nHealth officials are advising consumers to take extreme caution with any purchased fruit, but if you’d rather not throw strawberries out, there are plenty of ways to use them up.", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcS_NnUS0UEFSwwmWk08T15vittAlLZgedVd6w&usqp=CAU", BigDecimal.valueOf(5.99))
    );

    @Autowired
    public DataInit(CategoryService categoryService, RoleService roleService, ProductService productService, AuthService authService, InitRootUser initRootUser) {
        this.categoryService = categoryService;
        this.roleService = roleService;
        this.productService = productService;
        this.authService = authService;
        this.initRootUser = initRootUser;
    }

    @Override
    public void run(String... args) throws Exception {
        this.categoryService.initCategories();
        this.roleService.seedRolesInDb();
        this.authService.initRoot(initRootUser);
        this.productService.initProducts(PRODUCTS, initRootUser.getUsername());
    }
}
