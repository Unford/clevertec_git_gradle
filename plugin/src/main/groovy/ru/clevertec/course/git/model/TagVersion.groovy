package ru.clevertec.course.git.model


import java.util.regex.Matcher
import java.util.regex.Pattern

final class TagVersion {
    private final static String VERSION_PATTERN = "^v(\\d+\\.\\d+)(%s|%s)?\$"
            .formatted(VersionType.STAGE.getPostfix(), VersionType.OTHER.getPostfix())
    public final static TagVersion ZERO_VERSION = new TagVersion(0, 0, VersionType.MASTER)

    final int majorVersion
    final int minorVersion

    final VersionType versionType

    TagVersion(int majorVersion, int minorVersion, VersionType versionType) {
        this.majorVersion = majorVersion
        this.minorVersion = minorVersion
        this.versionType = versionType
    }

    int getMajorVersion() {
        return majorVersion
    }

    int getMinorVersion() {
        return minorVersion
    }

    VersionType getVersionType() {
        return versionType
    }

    TagVersion incrementMajorVersion() {
        return new TagVersion(majorVersion + 1, minorVersion, versionType)
    }

    TagVersion incrementMinorVersion() {
        return new TagVersion(majorVersion, minorVersion + 1, versionType)
    }

    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("v")
        sb.append(majorVersion).append('.').append(minorVersion)
        sb.append(versionType.getPostfix())
        return sb.toString()
    }

    static Optional<TagVersion> parseFromString(String str) {

        Pattern pattern = Pattern.compile(VERSION_PATTERN)
        Matcher matcher = pattern.matcher(str)
        if (matcher.matches()) {
            String[] versionNumber = matcher.group(1).split("\\.")
            String postfix = matcher.group(2)
            int majorV = Integer.parseInt(versionNumber[0])
            int minorV = Integer.parseInt(versionNumber[1])

            VersionType type = VersionType.DEV_OR_QA
            if (postfix != null) {
                switch (postfix) {
                    case VersionType.OTHER.getPostfix(): {
                        type = VersionType.OTHER
                        break
                    }
                    case VersionType.STAGE.getPostfix(): {
                        type = VersionType.STAGE
                        break
                    }
                }

            } else if (minorV == 0) {
                type = VersionType.MASTER
            }

            return Optional.of(new TagVersion(majorV, minorV, type))
        }
        return Optional.empty()
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof TagVersion)) return false

        TagVersion that = (TagVersion) o

        if (majorVersion != that.majorVersion) return false
        if (minorVersion != that.minorVersion) return false
        if (versionType != that.versionType) return false

        return true
    }

    int hashCode() {
        int result
        result = majorVersion
        result = 31 * result + minorVersion
        result = 31 * result + (versionType != null ? versionType.hashCode() : 0)
        return result
    }
}


