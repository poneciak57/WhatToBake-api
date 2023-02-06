<style>
ep r { color: Red }
ep y { color: Yellow }
ep g { color: Green }
ep{
    background-color: #222;
    padding: 3px 10px;
    border-radius: 3px;
    font-size: 16px
}

</style>

<img src="./docs/assets/what2bake-baner2.png">



You have some products but you dont know **what to bake**?
With this brand new app users will be able to search for recipes and products with advanced filters 
for example by the products they have, the recipes they like, categories, tags and so much more. App will be accesible in the web browser and on mobiles. 

# :books: Contents

- [Tech stack][tstack]
- [Docs][docs]
  - [Auth][auth]
  - [API][api]




# :hammer_and_wrench: Technologies & tools


# :clipboard: Docs
The api is up and running. Can be found here:
> http://132.226.204.66:81

To test the api u can hit the ping endpoint with get request

<ep>
    <g>GET</g> /test/ping
</ep>

---
## :lock: Auth
I use pocketbase as user service. Its not yet connected to listen for user changes in order to update the main database with new users. 

---
## :globe_with_meridians: API

Here you can find all endpoints that the api provide to the user.

<details>
<summary>Recipes</summary>


</details>
<details>
<summary>Products</summary>


</details>
<details>
<summary>Categories</summary>


</details>
<summary>Tags</summary>


</details>


Docs are under development 🚧

[tstack]: #hammer_and_wrench-technologies--tools
[docs]: #clipboard-docs
[auth]: #lock-auth
[api]: #globe_with_meridians-api