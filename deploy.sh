#!/bin/bash
# FSTBANK Quick Start Script for WildFly Deployment

echo "╔════════════════════════════════════════════════════════════╗"
echo "║   FSTBANK - Enterprise Banking Application                 ║"
echo "║   Quick Deployment Script for WildFly                      ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if Java is installed
echo -e "${YELLOW}[1] Checking Java installation...${NC}"
if ! command -v java &> /dev/null; then
    echo -e "${RED}✗ Java not found. Please install Java 11+${NC}"
    exit 1
fi
JAVA_VERSION=$(java -version 2>&1 | grep -oP 'version "?\K[0-9]+' | head -1)
echo -e "${GREEN}✓ Java ${JAVA_VERSION} found${NC}"
echo ""

# Check if Maven is installed
echo -e "${YELLOW}[2] Checking Maven installation...${NC}"
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}✗ Maven not found. Please install Maven 3.6+${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Maven found${NC}"
echo ""

# Build the application
echo -e "${YELLOW}[3] Building FSTBANK application...${NC}"
mvn clean package -DskipTests
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Build successful${NC}"
else
    echo -e "${RED}✗ Build failed${NC}"
    exit 1
fi
echo ""

# Check if WildFly is running
echo -e "${YELLOW}[4] Checking WildFly server...${NC}"
if curl -s http://localhost:8080 > /dev/null; then
    echo -e "${GREEN}✓ WildFly is running${NC}"
else
    echo -e "${YELLOW}⚠ WildFly is not running${NC}"
    echo "   Start WildFly with: ~/wildfly-20.0.0.Final/bin/standalone.sh"
    echo ""
fi
echo ""

# Deploy the application
echo -e "${YELLOW}[5] Deploying to WildFly...${NC}"
ARTIFACT="target/fstbank-ear-1.0.0.ear"

if [ -f "$ARTIFACT" ]; then
    cp "$ARTIFACT" ~/wildfly-20.0.0.Final/standalone/deployments/
    echo -e "${GREEN}✓ Application deployed${NC}"
else
    echo -e "${RED}✗ EAR file not found at ${ARTIFACT}${NC}"
    exit 1
fi
echo ""

# Display access information
echo -e "${YELLOW}[6] Access Information:${NC}"
echo "   Dashboard: ${GREEN}http://localhost:8080/fstbank${NC}"
echo "   Admin Console: ${GREEN}http://localhost:9990${NC}"
echo "   API Endpoint: ${GREEN}http://localhost:8080/fstbank/api/dashboard${NC}"
echo ""

echo -e "${GREEN}╔════════════════════════════════════════════════════════════╗"
echo "║  FSTBANK is ready! Open your browser and visit:              ║"
echo "║  ${GREEN}http://localhost:8080/fstbank${GREEN}                             ║"
echo "╚════════════════════════════════════════════════════════════╝${NC}"
