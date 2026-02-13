NAME = app

SRC_DIR = src
BIN_DIR = bin
BUILD_DIR = build

JAVAC = javac
JAVA = java
JAR = jar

JAVA_FX_VERSION = 17.0.16
JAVA_FX_PATH = lib/javafx-sdk-$(JAVA_FX_VERSION)/lib
JAVA_FX_ZIP = openjfx-$(JAVA_FX_VERSION)_linux-x64_bin-sdk.zip
JAVA_FX_URL = https://download2.gluonhq.com/openjfx/$(JAVA_FX_VERSION)/$(JAVA_FX_ZIP)
JAVA_FX_MODULES = javafx.controls,javafx.graphics

SOURCES = $(wildcard $(SRC_DIR)/*.java $(SRC_DIR)/Node/*.java $(SRC_DIR)/Algorithm/*.java)

all: check_javafx $(NAME) 

$(NAME): $(SOURCES)
	mkdir -p $(BIN_DIR) $(BUILD_DIR)
	$(JAVAC) \
		--module-path $(JAVA_FX_PATH) \
		--add-modules $(JAVA_FX_MODULES) \
		-d $(BIN_DIR) $(SOURCES)
	$(JAR) cfm $(NAME).jar manifest.txt -C $(BIN_DIR) .
	chmod +x $(NAME).jar
	$(JAVA) --module-path $(JAVA_FX_PATH) --add-modules $(JAVA_FX_MODULES) -jar $(NAME).jar

clean:
	rm -rf $(BIN_DIR) $(BUILD_DIR)

fclean: clean
	rm -f $(NAME) $(NAME).jar

re: fclean all

check_javafx:
	@if [ ! -d "$(JAVA_FX_PATH)" ]; then \
		echo "JavaFX not found at $(JAVA_FX_PATH)."; \
		echo "Either change the path in the Makefile or download it."; \
		read -p "Would you like to install it? [y/N] " ans; \
		if [ "$$ans" = "y" ]; then \
			echo "Downloading JavaFX..."; \
			mkdir -p lib; \
			curl -L -o lib/$(JAVA_FX_ZIP) $(JAVA_FX_URL); \
			if unzip -q lib/$(JAVA_FX_ZIP) -d lib; then \
				rm lib/$(JAVA_FX_ZIP); \
				echo "JavaFX installed successfully."; \
			else \
				echo "Error: 'unzip' failed or is not installed."; \
				echo "The archive is kept at lib/$(JAVA_FX_ZIP). Please install 'unzip' and extract manually."; \
				exit 1; \
			fi; \
		else \
			echo "Aborted. Please set JAVA_FX_PATH correctly."; \
			exit 1; \
		fi \
	else \
		echo "JavaFX already installed at $(JAVA_FX_PATH)"; \
	fi


.PHONY: all clean fclean re run