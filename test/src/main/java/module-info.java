module test {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    opens test.test to javafx.fxml;
    exports test.test;
}