/**
=========================================================

██╗░░██╗░█████╗░██╗░░░██╗
██║░██╔╝██╔══██╗╚██╗░██╔╝
█████═╝░███████║░╚████╔╝░
██╔═██╗░██╔══██║░░╚██╔╝░░
██║░╚██╗██║░░██║░░░██║░░░
╚═╝░░╚═╝╚═╝░░╚═╝░░░╚═╝░░░
 =========================================================

*/

// @mui material components
import Grid from "@mui/material/Grid";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";

// Material Dashboard 2 React example components
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import DashboardNavbar from "examples/Navbars/DashboardNavbar";
import Footer from "examples/Footer";

import BookCard from "bookstore-components/bookCard";

import cover from "assets/images/cover_harry-potter.jpg"

import { useEffect, useState } from "react";
import bamazonService from "services/bamazon-service";
import { Button } from "@mui/material";


function BookCatalogue() {
  const [books, setBooks] = useState([]);

  useEffect(() => {
    bamazonService.getBooks().then(res => {
      setBooks(res.data);
    }).catch(err => {
      console.log("Error fetching books: ", err);
    })
  }, [])

  return (
    <DashboardLayout>
      <DashboardNavbar />
      {books.map(({ id, title, author, rating, reviewCount, price, imgLocation }) => {
        return (
          <MDBox py={3} key={id}>
            <BookCard color="dark"
              bookId={id}
              title={title}
              author={author}
              imgLocation={imgLocation}
              rating={rating}
              price={price}
              reviewCount={reviewCount}
            />
          </MDBox>)
      })}
      <Footer />
    </DashboardLayout>
  );
}

export default BookCatalogue;
