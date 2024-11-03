class Node {
    int x, y, dx, dy;
    Node parent;

    Node(int x, int y, Node parent, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.parent = parent;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Node)) return false;
        Node other = (Node) obj;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode() {
        return x * 31 + y;
    }

    public void print() {
        System.out.printf("x %d : y %d\n", x, y);
        System.out.printf("dx %d : dy %d\n\n", dx, dy);
    }
}